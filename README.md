[![website](https://img.shields.io/badge/Website-w3id.org/launuts-a9d4e8)](https://w3id.org/launuts)
[![issues](https://img.shields.io/badge/Issue%20tracker-GitHub-d1e28a)](https://github.com/adibaba/LauNuts/issues)


# LauNuts

LauNuts is a RDF Knowledge Graph consisting of:

- Local Administrative Units (LAU) and
- Nomenclature of Territorial Units for Statistics (NUTS)

It is a hierarchical system where geographical areas are subdivided according to their population sizes.
This repository provides a generator to create a RDF Knowledge Graph from Excel input data.
Additional resources are available at [w3id.org/launuts](https://w3id.org/launuts).


## Software

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


## Installation

To run the Software, [Java](https://www.java.com/en/download/manual.jsp) has to be installed on your System.  
If you want to convert Excel data, please have a look into [data processing](docs/processing.md).


## Documentation

- [Data processing pipline](docs/processing.md)
- [Development](docs/development.md)
- [Related ressources](docs/resources.md)
- [Additional notes](docs/notes.md)


## Acknowledgements

This work has been supported by the German Federal Ministry of Education and Research (BMBF) within the project [EML4U](https://eml4u.github.io/) under the grant no 01IS19080B and by the German Federal Ministry of Transport and Digital Infrastructure (BMVI) within the project [OPAL](https://arxiv.org/pdf/2105.03161.pdf) under the grant no 19F2028A. 


## Credits

[Data Science Group (DICE)](https://dice-research.org/) at [Paderborn University](https://www.uni-paderborn.de/), Adrian Wilke