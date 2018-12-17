const { readFileSync: readFile, existsSync: exists, lstatSync: stat, mkdirSync: mkdir, writeFileSync: writeFile } = require('fs');
const { regex: { url: regex_url }, defaults: { settings: dfltSettings } }                                         = require('./bin/constants');

const { iterationData: vData, settings: vSettings } = require('./bin/validators');
const { bold, red, yellow, green, magenta }         = require('kleur');
const { resolve, join, dirname }                    = require('path');

const { merge } = require('./bin/utils');
const prompts   = require('prompts');
const yargs     = require('yargs');
const run       = require('./lib');

const { argv } = yargs
    .option('p', {
        alias: 'prompt',
        describe: 'Step by step configuration',
        conflicts: ['u', 'c', 'a', 'r'],
        type: 'boolean'
    })
    .option('d', {
        alias: ['data', 'iteration-data'],
        describe: 'Path to iteration data file',
        requiresArg: true,
        type: 'string'
    })
    .option('c', {
        alias: ['s', 'config', 'settings'],
        describe: 'Path to the configuration file',
        requiresArg: true,
        conflicts: ['u', 'a', 'r'],
        coerce(arg) {
            let { error, value: settings } = vSettings
                .validate(parseImport(arg), { stripUnknown: true });
            if (error) throw error;
            return JSON.stringify(settings);
        },
        type: 'string'
    })
    .option('u', {
        alias: ['url', 'base-url'],
        describe: 'The base url of the requests',
        example: 'http://localhost:8080/rest',
        requiresArg: true,
        coerce(arg) {
            if (!regex_url.test(arg))
                throw new Error('Specified URL is invalid');
            return arg;
        },
        type: 'string'
    })
    .option('r', {
        alias: ['ratio', 'success-rate'],
        describe: 'Minimum success ratio to continue',
        requiresArg: true,
        coerce(arg) {
            if (Number.isNaN(arg = Number(arg)) || !(arg <= 1 && arg >= 0))
                throw new Error('Success ratio must be between 0 and 1 inclusively');
            return arg;
        },
        type: 'number'
    })
    .option('a', {
        alias: ['confirm', 'always-confirm'],
        describe: 'Prompt for confirmation before every run',
        type: 'boolean'
    })
    .option('v', {
        alias: 'verbose',
        describe: 'Verbose output',
        type: 'boolean'
    })
    .alias('V', 'version')
    .alias('h', ['?', 'help'])
    .group(['p'], 'Recommended')
    .group(['d'], 'Required')
    .group(['c', 'u', 'r', 'a'], 'Configure')
    .group(['v', 'h', 'V'], 'Other')
    .usage('$0 -d <data> [-u <url>] [-r <ratio>] [-a] [-v]')
    .usage('$0 -d <data> [-c <settings>]')
    .wrap(Math.min(yargs.terminalWidth(), 90))
    .check((argv, opts) => {
        if (Object.keys(opts).every(opt => argv[opt] === undefined))
            return new Error('no option specified');
        return true;
    }, true)
    .recommendCommands()
    .help();

function parseImport(arg) {
    let obj;
    
    if (exists(arg = resolve(arg)) && stat(arg).isDirectory()) {
        arg = exists(join(arg, 'index.json'))
              ? join(arg, 'index.json')
              : join(arg, 'index.js');
    }
    
    if (!exists(arg)) throw new Error('File not found!');
    
    try {
        obj = JSON.parse(readFile(arg, 'utf-8'));
    } catch (e) {
        try {
            obj = require(arg);
        } catch (e) {
            throw new Error(`Couldn't import ${arg}`);
        }
    }
    return obj;
}

function abortConfig() {
    console.log(bold().yellow('Configuration cancelled!'));
    process.exit(0);
}

(async () => {
    try {
        let settings = {};
        let { data } = argv;
        
        if (!data) {
            data = (await prompts({
                type: 'text',
                name: 'data',
                message: `What's the path to the iteration data file?`,
                initial: './'
            }, { onCancel: abortConfig })).data;
        }
        
        
        if (!Array.isArray(data = parseImport(data))) data = [data];
        data = await vData.validate(data, { stripUnknown: true });
        
        if (argv.prompt) {
            settings = await prompts([
                {
                    type: 'text',
                    name: 'baseURL',
                    message: `What's the base URL of the requests?`,
                    initial: dfltSettings.baseURL,
                    validate(input) {
                        input = (input.startsWith('/') ? 'http://localhost:8080' : '') + input;
                        return regex_url.test(input);
                    },
                    format(input) {
                        return (input.startsWith('/') ? 'http://localhost:8080' : '') + input;
                    }
                }, {
                    type: 'number',
                    name: 'minSuccessRate',
                    message: `What's the minimum ratio needed?`,
                    float: true,
                    initial: dfltSettings.minSuccessRate,
                    min: 0,
                    max: 1,
                    round: 4,
                    increment: .1
                }, {
                    type: 'toggle',
                    name: 'alwaysConfirm',
                    message: 'Do you want to confirm before every collection?',
                    initial: dfltSettings.alwaysConfirm,
                    active: 'yes',
                    inactive: 'no'
                }, {
                    type: 'toggle',
                    name: 'verbose',
                    message: 'Show detailed information? (verbose)',
                    inital: argv.verbose || dfltSettings.verbose,
                    active: 'yes',
                    inactive: 'no'
                }
            ], { onCancel: abortConfig });
            
            const url = green(settings.baseURL),
                  msr = yellow(settings.minSuccessRate),
                  alc = magenta(settings.alwaysConfirm ? 'yes' : 'no'),
                  vrb = magenta(settings.verbose ? 'yes' : 'no');
            
            console.log();
            console.log(bold().blue('                           ╔═════════════════════╗'));
            console.log(bold().blue('══╦════════════════════════╣ Final Configuration ║'));
            console.log(bold().blue('  ║                        ╚═════════════════════╝'));
            console.log(bold().blue(`  ╟─────────────⊳`) + ` Base URL: ${url}`);
            console.log(bold().blue(`  ╟────────⊳`) + ` Minimum ratio: ${msr}`);
            console.log(bold().blue(`  ╟───────⊳`) + ` Always confirm: ${alc}`);
            console.log(bold().blue(`  ╙─⊳`) + ` Detailed information: ${vrb}`);
            console.log();
            
            let { confirm } = await prompts({
                type: 'toggle',
                name: 'confirm',
                message: 'Do you confirm the above configuration?',
                initial: true,
                active: 'yes',
                inactive: 'no'
            }, { onCancel: abortConfig });
            if (!confirm) abortConfig();
            
            let { savePath } = await prompts([
                {
                    type: 'toggle',
                    name: 'save',
                    message: 'Do you want to save this configuration?',
                    initial: false,
                    active: 'yes',
                    inactive: 'no'
                }, {
                    type: prev => prev && 'text',
                    name: 'savePath',
                    message: 'Specify the file path where to save the configuration',
                    initial: './settings.json',
                    format: file => resolve(file),
                    validate: file => !exists(resolve(file))
                }
            ], { onCancel: abortConfig });
            
            if (savePath) {
                if (!exists(dirname(savePath)))
                    mkdir(dirname(savePath), { recursive: true });
                
                writeFile(savePath, JSON.stringify(settings), 'utf-8');
                console.log(bold().blue('File saved!'));
            }
        } else {
            settings = merge({ // Default settings
                    baseURL: dfltSettings.baseURL,
                    minSuccessRate: dfltSettings.minSuccessRate,
                    alwaysConfirm: dfltSettings.alwaysConfirm,
                    verbose: dfltSettings.verbose
                }, // Settings object
                JSON.parse(argv.settings || '{}'),
                { // Setting arguments
                    baseURL: argv.url,
                    minSuccessRate: argv.ratio,
                    alwaysConfirm: argv.confirm,
                    verbose: argv.verbose
                });
        }
        
        console.log(bold().blue('Setup completed!\n'));
        await run(data, settings);
        
    } catch (err) {
        console.error(bold().red(err.message));
        if (argv.verbose) console.error(red(err.stack));
        process.exit(0);
    }
})();
