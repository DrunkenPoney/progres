const { bold, red, yellow, magenta, cyan } = require('kleur');

const newman    = require('newman');
const transform = require('./transform');
const prompts   = require('prompts');


function run(options) {
    return new Promise((resolve, reject) => {
        newman.run(options, (err, summary) => {
            if (err) reject(err);
            else resolve(summary);
        });
    });
}

module.exports = async function (iterationData, { minSuccessRate, alwaysConfirm, verbose, baseURL }) {
    try {
        let collections = iterationData
            .filter(({ disabled }) => !disabled)
            .map(a => Object.assign({ baseURL }, a))
            .sort((a, b) => a.order - b.order)
            .map(transform);
        
        for (let collection of collections) {
            console.log(bold().blue(`Next collection: `) + cyan(collection.name));
            if (alwaysConfirm) {
                let { skip } = await prompts({
                    type: "toggle",
                    name: "skip",
                    message: `Skip?`,
                    initial: false,
                    active: 'yes',
                    inactive: 'no'
                }, {
                    onCancel() {
                        console.log(bold().yellow('Aborted!'));
                        process.exit(0);
                    }
                });
                console.log();
                if (skip) continue;
            }
            
            console.log();
            
            let { run: result } = await run({
                collection,
                reporters: verbose ? "cli" : "progress",
                color: true
            });
            
            let nbFails   = result.failures.length,
                nbTotal   = result.executions.length,
                nbSuccess = nbTotal - nbFails,
                ratio     = nbSuccess / nbTotal;
            
            console.log();
            console.log(bold().blue('                 ╔════════════════════╗'));
            console.log(bold().blue('══╦══════════════╣ Collection Results ║'));
            console.log(bold().blue('  ║              ╚════════════════════╝'));
            console.log(bold().blue(`  ╟──────⊳`) + ` Total: ${yellow(nbTotal)}`);
            console.log(bold().blue(`  ╟───⊳`) + ` Failures: ${yellow(nbFails)}`);
            console.log(bold().blue(`  ╟────⊳`) + ` Success: ${yellow(nbSuccess)}`);
            console.log(bold().blue(`  ╙──────⊳`) + ` Ratio: ${yellow(ratio)}`);
            console.log();
            
            if (nbFails > 0 && nbTotal > 0 && (ratio < minSuccessRate)) {
                console.log(bold().yellow("Aborted! ")
                            + yellow(`Ratio too low: ${magenta(ratio)} < ${magenta(minSuccessRate)}`));
                process.exit(0);
            }
        }
        
        console.log(bold().green('Done!'));
        
    } catch (err) {
        console.error(bold().red(err.message));
        if (verbose) console.error(red(err.stack));
        process.exit(0);
    }
    process.exit(0);
};