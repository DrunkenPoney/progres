function initVolumeUnits() {
    const apiUrl = "http://localhost:8080/rest/volume-unit/add";
    const data = [
        {
            title: "M\u00e8tre(s) cube(s)",
            key: "CUBIC_METER",
            unit: "m\u00b3",
            mod: 1
        },
        {
            title: "Kilom\u00e8tre(s) cube(s)",
            key: "CUBIC_KILOMETER",
            unit: "km\u00b3",
            mod: 1000000000
        },
        {
            title: "D\u00e9cim\u00e8tre(s) cube(s)",
            key: "CUBIC_DECIMETER",
            unit: "dm\u00b3",
            mod: 0.001
        },
        {
            title: "Centim\u00e8tre(s) cube(s)",
            key: "CUBIC_CENTIMETER",
            unit: "cm\u00b3",
            mod: 0.000001
        },
        {
            title: "Millim\u00e8tre(s) cube(s)",
            key: "CUBIC_MILLIMETER",
            unit: "mm\u00b3",
            mod: 1e-9
        },
        {
            title: "Litre(s)",
            key: "LITER",
            unit: "L",
            mod: 0.001
        },
        {
            title: "Millilitre(s)",
            key: "MILLILITER",
            unit: "mL",
            mod: 0.000001
        },
        {
            title: "Baril(s) US",
            key: "BARREL_US",
            unit: "bbl (US)",
            mod: 0.1192404712
        },
        {
            title: "Gallon(s) US",
            key: "GALLON_US",
            unit: "gal (US)",
            mod: 0.00037854118
        },
        {
            title: "Quart(s) US",
            key: "QUART_US",
            unit: "qt (US)",
            mod: 0.00009463529
        },
        {
            title: "Pinte(s) US",
            key: "PINT_US",
            unit: "pt (US)",
            mod: 0.00004731765
        },
        {
            title: "Tasse(s) US",
            key: "CUP_US",
            unit: "t (US)",
            mod: 0.00002365882
        },
        {
            title: "Once(s) liquide(s) US",
            key: "FLUID_OUNCE_US",
            unit: "fl oz (US)",
            mod: 0.0000295735
        },
        {
            title: "Cuilli\u00e8re(s) \u00e0 table US",
            key: "TABLESPOON_US",
            unit: "c. \u00e0 table (US)",
            mod: 0.0000147868
        },
        {
            title: "Cuill\u00e8re(s) \u00e0 d\u00e9ssert US",
            key: "DESSERTSPOON_US",
            unit: "c. \u00e0 d\u00e9ssert (US)",
            mod: 0.0000098578431875
        },
        {
            title: "Cuill\u00e8re(s) \u00e0 th\u00e9 US",
            key: "TEASPOON_US",
            unit: "c. \u00e0 th\u00e9 (US)",
            mod: 0.00000492892159375
        },
        {
            title: "Mille(s) cube(s)",
            key: "CUBIC_MILE",
            unit: "mi\u00b3",
            mod: 4168181825.4406
        },
        {
            title: "Verge(s) cube(s)",
            key: "CUBIC_YARD",
            unit: "vg\u00b3",
            mod: 0.764554858
        },
        {
            title: "Pied(s) cube(s)",
            key: "CUBIC_FOOT",
            unit: "pi\u00b3",
            mod: 0.00283168466
        },
        {
            title: "Pousse(s) cube(s)",
            key: "CUBIC_INCH",
            unit: "po\u00b3",
            mod: 0.0000163871
        },
        {
            title: "Exalitre(s)",
            key: "EXALITER",
            unit: "EL",
            mod: 1000000000000000
        },
        {
            title: "Petalitre(s)",
            key: "PETALITER",
            unit: "PT",
            mod: 1000000000000
        },
        {
            title: "Teralitre(s)",
            key: "TERALITER",
            unit: "TL",
            mod: 1000000000
        },
        {
            title: "Gigalitre(s)",
            key: "GIGALITER",
            unit: "GL",
            mod: 1000000
        },
        {
            title: "Megalitre(s)",
            key: "MEGALITER",
            unit: "ML",
            mod: 1000
        },
        {
            title: "Kilolitre(s)",
            key: "KILOLITER",
            unit: "kL",
            mod: 1
        },
        {
            title: "Hectolitre(s)",
            key: "HECTOLITER",
            unit: "hL",
            mod: 0.1
        },
        {
            title: "Decalitre(s)",
            key: "DEKALITER",
            unit: "daL",
            mod: 0.01
        },
        {
            title: "Decilitre(s)",
            key: "DECILITER",
            unit: "dL",
            mod: 0.0001
        },
        {
            title: "Centilitre(s)",
            key: "CENTILITER",
            unit: "cL",
            mod: 0.00001
        },
        {
            title: "Microlitre(s)",
            key: "MICROLITER",
            unit: "\u00b5L",
            mod: 1e-9
        },
        {
            title: "Nanolitre(s)",
            key: "NANOLITER",
            unit: "nL",
            mod: 1e-12
        },
        {
            title: "Picolitre(s)",
            key: "PICOLITER",
            unit: "pL",
            mod: 1e-15
        },
        {
            title: "Femtolitre(s)",
            key: "FEMTOLITER",
            unit: "fL",
            mod: 1e-18
        },
        {
            title: "Attolitre(s)",
            key: "ATTOLITER",
            unit: "aL",
            mod: 1e-21
        },
        {
            title: "CC",
            key: "CC",
            unit: "cc",
            mod: 0.000001
        },
        {
            title: "Goutte(s)",
            key: "DROP",
            unit: "goutte(s)",
            mod: 5e-8
        },
        {
            title: "Baril(s) UK",
            key: "BARREL_UK",
            unit: "bbl (UK)",
            mod: 0.16365924
        },
        {
            title: "Gallon(s) UK",
            key: "GALLON_UK",
            unit: "gal (UK)",
            mod: 0.00454609
        },
        {
            title: "Quart(s) UK",
            key: "QUART_UK",
            unit: "qt (UK)",
            mod: 0.0011365225
        },
        {
            title: "Pinte(s) UK",
            key: "PINT_UK",
            unit: "pt (UK)",
            mod: 0.0005682613
        },
        {
            title: "Tasse(s) UK",
            key: "CUP_UK",
            unit: "t (UK)",
            mod: 0.0002841306
        },
        {
            title: "Once(s) liquide(s) UK",
            key: "FLUID_OUNCE_UK",
            unit: "fl oz (UK)",
            mod: 0.0000284131
        },
        {
            title: "Cuilli\u00e8re(s) \u00e0 table",
            key: "TABLESPOON",
            unit: "c. \u00e0 table",
            mod: 0.000015
        },
        {
            title: "Cuilli\u00e8re(s) \u00e0 table UK",
            key: "TABLESPOON_UK",
            unit: "c. \u00e0 table (UK)",
            mod: 0.0000177582
        },
        {
            title: "Cuill\u00e8re(s) \u00e0 d\u00e9ssert UK",
            key: "DESSERTSPOON_UK",
            unit: "c. \u00e0 d\u00e9ssert (UK)",
            mod: 0.0000118388
        },
        {
            title: "Cuill\u00e8re(s) \u00e0 th\u00e9 UK",
            key: "TEASPOON_UK",
            unit: "c. \u00e0 th\u00e9 (UK)",
            mod: 0.0000059193880208333
        },
        {
            title: "Tasse(s)",
            key: "CUP",
            unit: "t",
            mod: 0.00025
        },
        {
            title: "Cuill\u00e8re(s) \u00e0 th\u00e9",
            key: "TEASPOON",
            unit: "c. \u00e0 th\u00e9",
            mod: 0.000005
        },
        {
            title: "St\u00e8re(s)",
            key: "STERE",
            unit: "st",
            mod: 1
        },
        {
            title: "Corde(s)",
            key: "CORD",
            unit: "cd",
            mod: 3.6245563638
        },
        {
            title: "Drachme(s)",
            key: "DRAM",
            unit: "dr",
            mod: 0.0000036966911953125
        },
        {
            title: "Plan\u00e8te(s) Terre",
            key: "EARTH",
            unit: "terre(s)",
            mod: 1.083e+21
        }
    ];
    
    return Promise.all(data.map(unit => {
        return fetch(apiUrl, {
            method: 'POST',
            body: JSON.stringify(unit),
            mode: 'no-cors',
            headers: {
                // 'Accept': 'application/json;charset=utf-8',
                'Content-Type': 'application/json'
            }
        }).then(resp => resp.text());
    }))
}