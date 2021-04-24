#!/bin/bash

# LauNuts extraction of NUTS and LAU data
# DICE group, Adrian Wilke
# https://github.com/projekt-opal/LauNuts

DLDIR="../data/download/"
CSVDIR="../data/csv/"

# Get all files
# https://stackoverflow.com/a/32931403
IFS=$'\n' read -r -d '' -a FILES < <( find $DLDIR && printf '\0' )

# Extract spreadsheets
# https://github.com/dilshod/xlsx2csv
# Installation: pip install xlsx2csv
# Command: xlsx2csv -s 0 file.xlsx dir/
for FILE in ${FILES[@]}; do
  if [[ $FILE == *xlsx ]] ;
  then
    DLPATH=$CSVDIR${FILE:${#DLDIR}}
    echo $FILE
    echo $DLPATH
    echo ""
    xlsx2csv -s 0 $FILE $DLPATH
  fi
done

