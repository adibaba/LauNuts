#!/bin/bash

# LauNuts download of NUTS and LAU data
# DICE group, Adrian Wilke
# https://github.com/projekt-opal/LauNuts

DIR="download/"
LOG="download.log"

# https://www.gnu.org/software/wget/manual/wget.html#Download-Options
# -a  --append-output=logfile
# -c  --continue
# -nv --no-verbose
# -P  --directory-prefix=prefix

# https://linuxize.com/post/bash-functions/
function set_directory {
  local DLDIR=$DIR$1/
  DL="wget -a $DLDIR$LOG -c -P $DLDIR -nv "
  mkdir -p $DLDIR
}

# NUTS
set_directory "nuts"
# https://ec.europa.eu/eurostat/web/nuts/history
$DL https://ec.europa.eu/eurostat/documents/345175/629341/NUTS2021.xlsx
$DL https://ec.europa.eu/eurostat/documents/345175/629341/NUTS2013-NUTS2016.xlsx
$DL https://ec.europa.eu/eurostat/documents/345175/629341/NUTS+2010+-+NUTS+2013.xls
$DL https://ec.europa.eu/eurostat/documents/345175/629341/2006-2010.xls
$DL https://ec.europa.eu/eurostat/documents/345175/629341/2003-2006.xls
$DL https://ec.europa.eu/eurostat/documents/345175/629341/1999-2003.xls
$DL https://ec.europa.eu/eurostat/documents/345175/629341/1995-1999.xls

# LAU
set_directory "lau"
# https://ec.europa.eu/eurostat/web/nuts/local-administrative-units
$DL https://ec.europa.eu/eurostat/documents/345175/501971/EU-27-LAU-2020-NUTS-2021-NUTS-2016.xlsx
$DL https://ec.europa.eu/eurostat/documents/345175/501971/EU-28-LAU-2019-NUTS-2016.xlsx
$DL https://ec.europa.eu/eurostat/documents/345175/501971/EU-28-LAU-2018-NUTS-2016.xlsx
$DL https://ec.europa.eu/eurostat/documents/345175/501971/EU-28_LAU_2017_NUTS_2016.xlsx
$DL https://ec.europa.eu/eurostat/documents/345175/501971/EU-28_LAU_2017_NUTS_2013.xlsx
$DL https://ec.europa.eu/eurostat/documents/345175/501971/EU-28_LAU_2016
$DL https://ec.europa.eu/eurostat/documents/345175/501971/EU-28_2015.xlsx
$DL https://ec.europa.eu/eurostat/documents/345175/501971/EU-28_2014.xlsx
$DL https://ec.europa.eu/eurostat/documents/345175/501971/EU-28_2013.xlsx
$DL https://ec.europa.eu/eurostat/documents/345175/501971/EU-28_2012.xlsx
$DL https://ec.europa.eu/eurostat/documents/345175/501971/EU-28_2011_Census.xlsx
$DL https://ec.europa.eu/eurostat/documents/345175/501971/EU-27_2011.xlsx
$DL https://ec.europa.eu/eurostat/documents/345175/501971/EU-27_2010.xlsx

# NUTS RDF
set_directory "nuts-rdf"
# https://ec.europa.eu/eurostat/web/nuts/linked-open-data
# https://data.europa.eu/euodp/en/data/dataset/ESTAT-NUTS-classification
$DL https://data.europa.eu/euodp/repository/ec/estat/nuts/nuts.rdf

# Geo
set_directory "geo"
# https://ec.europa.eu/eurostat/web/nuts/linked-open-data
# https://data.europa.eu/euodp/en/data/dataset/ESTAT-NUTS-classification
# https://ec.europa.eu/eurostat/web/gisco/geodata/reference-data/administrative-units-statistical-units
# https://ec.europa.eu/eurostat/web/gisco/geodata/reference-data/administrative-units-statistical-units/nuts#nuts21
 #$DL https://gisco-services.ec.europa.eu/distribution/v2/nuts/download/ref-nuts-2021-01m.shp.zip
 #$DL https://gisco-services.ec.europa.eu/distribution/v2/nuts/download/ref-nuts-2021-01m.geojson.zip
# https://ec.europa.eu/eurostat/web/gisco/geodata/reference-data/administrative-units-statistical-units/lau#lau19
 #$DL https://gisco-services.ec.europa.eu/distribution/v2/lau/download/ref-lau-2019-01m.shp.zip
 #$DL https://gisco-services.ec.europa.eu/distribution/v2/lau/download/ref-lau-2019-01m.geojson.zip

