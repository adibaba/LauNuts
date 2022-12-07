[![Issues](https://img.shields.io/github/issues/adibaba/LauNuts)](https://github.com/adibaba/LauNuts/issues)


# LauNuts

LauNuts is a RDF Knowledge Graph consisting of:

- Local Administrative Units (LAU) and
- Nomenclature of Territorial Units for Statistics (NUTS)


## LauNuts Knowledge Graph

The LauNuts Knowledge Graph is based on the *Eurostat: NUTS - Linked Open Data* dataset scheme and extends it by LAU data.


## LauNuts Generator

The software in this repository can be executed as follows:

```
Usage:   COMMAND [OPTIONS] MODE
Example: java -jar launuts.jar -ids "nuts-2016-2021 lau2021-nuts2021" dl
Modes:
  ls:   Lists available dataset IDs
  dl:   Downloads Excel files
  csv:  Converts Excel files to CSV
  ct:   Lists available LAU countries
  kg:   Create knowledge graph
  help: Prints this help
Options:
 -countries <"C1 C2 ...">
 -ids <"ID1 ID2 ...">
```

## Documentation

- [Data processing pipline](docs/processing.md)
- [Related ressources](docs/resources.md)
- [Additional nots](docs/notes.md)


## Credits

[Data Science Group (DICE)](https://dice-research.org/) at [Paderborn University](https://www.uni-paderborn.de/), Adrian Wilke