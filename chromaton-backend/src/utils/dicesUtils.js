const createDices = (playersCount) => {
    let dices = [];

    let config = {
        dicePerColor: 8,
        colors: ['green', 'blue', 'purple', 'red', 'orange', 'yellow'],
        secretSetDiceCount: 6,
        hand: 2
    };

    config = getModifiedConfig(playersCount, config);

    const diceCount = config.dicePerColor * config.colors.length;

    for (let i = 0; i < diceCount; i++) {
        const colorIndex = Math.floor(i / config.dicePerColor);
        dices.push({
            color: config.colors[colorIndex],
            location: 'bag' // Por defecto todos en la bolsa
        });
    }

    const shuffledDices = dices.sort(() => Math.random() - 0.5); //mezclo todos los dados y luego ajusto los primeros

    const secretTotal = config.secretSetDiceCount * playersCount;
    for (let i = 0; i < secretTotal; i++) {
        shuffledDices[i].location = 'secret_set';
    }

    const handTotal = config.hand * playersCount;
    for (let i = secretTotal; i < (secretTotal + handTotal); i++) {
        shuffledDices[i].location = 'hand';
    }

    //añado los dados negro y blanco si hay más de 2 jugadores
    if(playersCount === 2){
        return shuffledDices;
    } else {
        shuffledDices.push({
            color: 'white',
            location: 'bag'
        });
        shuffledDices.push({
            color: 'black',
            location: 'bag'
        });
        return shuffledDices;
    }
};

const getModifiedConfig = (playersCount, config) => {
    let newConfig = { ...config }; // Clonamos para no mutar el original

    switch (playersCount) {
        case 2:
newConfig.dicePerColor = 6;
            newConfig.colors.pop();
            break;
        case 3:
            newConfig.colors.pop();
            break;
        case 4:
            break;
        case 5:
            newConfig.secretSetDiceCount = 4;
            newConfig.colors.pop();
            break;
        case 6:
            newConfig.secretSetDiceCount = 4;
            break;
        case 7:
            newConfig.secretSetDiceCount = 3;
            break;
        case 8:
            newConfig.secretSetDiceCount = 3;
            break;

        case 9:
            newConfig.dicePerColor = 6;
            snewConfig.ecretSetDiceCount = 2;
            newConfig.hand = 1;
            break;
        case 10:
            newConfig.secretSetDiceCount = 2;
            newConfig.colors.pop();
            newConfig.hand = 1;
            break;
        default:
            return 'Número de jugadores incorrecto'
    }
    return newConfig;
};

let dices = createDices(11);

for (let i = 0; i < dices.length; i++) {
    console.log(`dado ${i + 1}: color ${dices[i].color}, location ${dices[i].location} `);
}