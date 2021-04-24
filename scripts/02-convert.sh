#!/bin/bash

# LauNuts convertion of NUTS and LAU data
# DICE group, Adrian Wilke
# https://github.com/projekt-opal/LauNuts

DIR="../data/download/nuts/"

pushd .
cd $DIR

# Get all files
# https://stackoverflow.com/a/32931403
IFS=$'\n' read -r -d '' -a FILES < <( find ./ && printf '\0' )

# Convert
for FILE in ${FILES[@]}; do
  if [[ $FILE == *xls ]] ;
  then
    DLPATH=$CSVDIR${FILE:${#DLDIR}}
    echo $FILE
    libreoffice --convert-to xlsx $FILE --headless
  fi
done

popd

