# LauNuts website

The website is available at [https://adibaba.github.io/LauNuts/](https://adibaba.github.io/LauNuts/) and [https://w3id.org/launuts](https://w3id.org/launuts). 
The related code is in repository [https://github.com/adibaba/LauNuts/tree/github.io](https://github.com/adibaba/LauNuts/tree/github.io).

It serves basic information and redirection for URIs based on the [generated model](https://github.com/adibaba/LauNuts/blob/master/src/main/java/org/dice_research/launuts/rdf/ModelBuilder.java):

- https://w3id.org/launuts/nuts/scheme#`nutsSchema`
    - https://w3id.org/launuts/nuts/scheme#2021
    - https://w3id.org/launuts/nuts/scheme#2016
- https://w3id.org/launuts/nuts/code#`nutsCode`
    - https://w3id.org/launuts/nuts/code#DE
- https://w3id.org/launuts/nuts/`nutsSchema`#`nutsCode`
    - https://w3id.org/launuts/nuts/2021#DE
- https://w3id.org/launuts/lau/`lauSchema`/`countryCode`#`lauCode`
    - https://w3id.org/launuts/lau/2021/DE#05774032
- https://w3id.org/launuts/lau/`countryCode`#`lauCode`;
    - https://w3id.org/launuts/lau/DE#05774032
- https://w3id.org/launuts/lau/`lauSchema`
    - https://w3id.org/launuts/lau/2021
    - https://w3id.org/launuts/lau/2020
- https://w3id.org/launuts/nutsScheme
- https://w3id.org/launuts/lauScheme
- https://w3id.org/launuts/level/0
- https://w3id.org/launuts/level/1
- https://w3id.org/launuts/level/2
- https://w3id.org/launuts/level/3

## Credits

[Data Science Group (DICE)](https://dice-research.org/) at [Paderborn University](https://www.uni-paderborn.de/), Adrian Wilke