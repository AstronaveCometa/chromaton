import pool from '../../db/config.js';

const diceNumbersByPlayerCount = () => {
    return {
        2: [{
            
        },
        {

        }],
        3: { secret: 3, hand: 2 },
        4: { secret: 2, hand: 2 },
        5: { secret: 2, hand: 1 },
        6: { secret: 1, hand: 1 },
    };
};


//createDiceInstancesForGameId(game_id)




//getHandByPlayerId(player_id)
//getSecretCodeByPlayerId(player_id)
//getHitsByPlayerId(player_id)
//getBagByGameId(game_id)

//compareDiceSets(attacker_dice_set, defender_dice_set)
//setHits(dice_instance_id)
