--Creaci�n de los ENUM
CREATE TYPE game_status AS ENUM ('waiting', 'playing', 'finished');
CREATE TYPE dice_color AS ENUM ('green', 'blue', 'purple', 'red', 'orange', 'yellow', 'black', 'white');
CREATE TYPE dice_location AS ENUM ('bag', 'secret_set', 'hand', 'rack');

CREATE TABLE users (
    user_id VARCHAR(128) PRIMARY KEY, 
    name VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) UNIQUE NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

SELECT * FROM users;

CREATE TABLE games (
    game_id SERIAL PRIMARY KEY,
    host_id VARCHAR(128) NOT NULL,
    status game_status NOT NULL DEFAULT 'waiting',
    current_turn_player_id INTEGER,
    round_count INTEGER DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    game_password VARCHAR(150),
    players_counter INTEGER DEFAULT 1,
    FOREIGN KEY (current_turn_player_id) REFERENCES players(player_id),
    FOREIGN KEY (host_id) REFERENCES users(user_id)
);

CREATE TABLE players (
    player_id SERIAL PRIMARY KEY,
    game_id INTEGER NOT NULL,
    user_id VARCHAR(128) NOT NULL,
    turn_order INTEGER,
    is_eliminated BOOLEAN DEFAULT false,
    has_white_dice BOOLEAN DEFAULT false,
    FOREIGN KEY (game_id) REFERENCES games(game_id),
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);

ALTER TABLE games
ADD CONSTRAINT fk_current_turn_player_id
FOREIGN KEY (current_turn_player_id)
REFERENCES players(player_id);

CREATE TABLE dice_instances (
    dice_instance_id SERIAL PRIMARY KEY,
    game_id INTEGER NOT NULL,
    color dice_color NOT NULL,
    value INTEGER NOT NULL,
    location dice_location NOT NULL,
    player_id INTEGER NOT NULL,
    is_revealed BOOLEAN DEFAULT false,
    FOREIGN KEY (game_id) REFERENCES games(game_id),
    FOREIGN KEY (player_id) REFERENCES players(player_id)
);