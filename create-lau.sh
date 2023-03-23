#!/bin/bash
#
# Get country codes:
#  cd /home/wilke/git/LauNuts/data/csv/lau2021-nuts2021
#  cd /home/wilke/git/LauNuts/data/csv/lau2020-nuts2016
#  ls -1 *.csv
#  Then only take files in the form of DE.csv

LAU_TEST=(XX XY)
LAU2021=(AL AT BA BE BG CH CY CZ DE DK EE EL ES FI FR HR HU IE IS IT LI LT LU LV ME MK MT NL NO PL PT RO RS SE SI SK TR UK XK)
LAU2020=(AL AT BA BG CH CY CZ DE DK EL ES FI FR HU IE IS LI LT LU LV ME MK MT NL NO PL PT RO RS SE SI SK TR XK)
LAU_ALL=(AL AT BA BE BG CH CY CZ DE DK EE EL ES FI FR HR HU IE IS IT LI LT LU LV ME MK MT NL NO PL PT RO RS SE SI SK TR UK XK)

echo LAU2021
echo ${LAU2021[@]}
echo LAU2020
echo ${LAU2020[@]}
echo LAU_ALL
echo ${LAU_ALL[@]}

for i in ${LAU2021[@]}; do
  #echo $i
  mkdir lau/2021/${i}
  cp nutsScheme/index.htm lau/2021/${i}
done

for i in ${LAU2020[@]}; do
  #echo $i
  mkdir lau/2020/${i}
  cp nutsScheme/index.htm lau/2020/${i}
done

for i in ${LAU_ALL[@]}; do
  #echo $i
  mkdir lau/${i}
  cp nutsScheme/index.htm lau/${i}
done
