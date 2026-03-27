--Creación de los ENUM
CREATE TYPE game_status AS ENUM ('waiting', 'playing', 'finished');
CREATE TYPE dice_color AS ENUM ('green', 'blue', 'purple', 'red', 'orange', 'yellow', 'black', 'white');
CREATE TYPE dice_location AS ENUM ('bag', 'secret_set', 'hand', 'rack');

CREATE TABLE users (
user_id SERIAL UNIQUE NOT NULL,
name VARCHAR(50) NOT NULL,
email VARCHAR(50) NOT NULL,
password VARCHAR(150) NOT NULL
);

SELECT * FROM users;

CREATE TABLE games (
game_id SERIAL UNIQUE NOT NULL,
host_id INTEGER NOT NULL,
status game_status NOT NULL DEFAULT 'waiting',
current_turn_player_id INTEGER,
round_count INTEGER DEFAULT 0,
created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
FOREIGN KEY (current_turn_player_id) REFERENCES players(player_id),
FOREIGN KEY (host_id) REFERENCES users(user_id)
);

CREATE TABLE players (
player_id SERIAL UNIQUE NOT NULL,
game_id INTEGER NOT NULL,
user_id INTEGER NOT NULL,
turn_order INTEGER,
is_eliminated BOOLEAN DEFAULT false,
has_white_dice BOOLEAN DEFAULT false,
FOREIGN KEY (game_id) REFERENCES games(game_id),
FOREIGN KEY (user_id) REFERENCES users(user_id)
);

CREATE TABLE dice_instances (
dice_instance_id SERIAL UNIQUE NOT NULL,
game_id INTEGER NOT NULL,
color dice_color NOT NULL,
value INTEGER NOT NULL,
location dice_location NOT NULL,
player_id INTEGER NOT NULL,
is_revealed BOOLEAN DEFAULT false,
FOREIGN KEY (game_id) REFERENCES games(game_id),
FOREIGN KEY (player_id) REFERENCES players(player_id)
);