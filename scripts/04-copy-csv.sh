#!/bin/bash

# LauNuts CSV copy of NUTS and LAU data
# DICE group, Adrian Wilke
# https://github.com/projekt-opal/LauNuts

# Commands to create lists of files:
# find csv/nuts | grep .csv > csv-nuts.txt
# find csv/lau | grep .csv > csv-lau.txt

mkdir -p data/nuts
cp csv/nuts/1995-1999.xlsx/NUTS1995-NUTS1999.csv data/nuts/nuts-1995-1999.csv
cp csv/nuts/1999-2003.xlsx/NUTS1999-NUTS2003.csv data/nuts/nuts-1999-2003.csv
cp csv/nuts/2003-2006.xlsx/NUTS2003-NUTS2006.csv data/nuts/nuts-2003-2006.csv
cp csv/nuts/2006-2010.xlsx/NUTS2006-NUTS2010.csv data/nuts/nuts-2006-2010.csv
cp csv/nuts/NUTS+2010+-+NUTS+2013.xlsx/NUTS2010-NUTS2013.csv data/nuts/nuts-2010-2013.csv
cp csv/nuts/NUTS2013-NUTS2016.xlsx/NUTS2013-NUTS2016.csv data/nuts/nuts-2013-2016.csv
cp csv/nuts/NUTS2021.xlsx/NUTS\ \&\ SR\ 2021.csv data/nuts/nuts-2021.csv

