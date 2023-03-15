# LauNuts development

## New data

The source data is updated by Eurostat. To check for new versions, have a look at the [NUTS](https://ec.europa.eu/eurostat/web/nuts/history) and [LAU](https://ec.europa.eu/eurostat/web/nuts/local-administrative-units) websites.

## Process new data

The generator software can handle upcoming data if the respective columns are named as in the past. If there are changes in the naming schemes, the respective Java files may have to be modified.

For NUTS, edit the method `searchHeadingColumns()` in `NutsCsvParser.java`.

For LAU, edit the method `searchHeadingColumns()` in `LauCsvParser.java`.

In general, you can use the `Dev.java` file to test code without modifing the general workflow.

## Release a new software version

To release a new software version, edit the string `<version>0.5.0-SNAPSHOT</version>` in `pom.xml`. It could be changed into `<version>0.5.0</version>`. Afterwards, create a GitHub tag/release. After a new software version is released, edit the file again and increment the version number into e.g. `<version>0.5.1-SNAPSHOT</version>`.

## Release a new knowledge graph version

New KGs should typically be named after the current NUTS version and with an additonal character to allow further updates, e.g. *LauNuts2021a*.
Update the website (branch [github.io](https://github.com/adibaba/LauNuts/tree/github.io)) and provide a changelog.
