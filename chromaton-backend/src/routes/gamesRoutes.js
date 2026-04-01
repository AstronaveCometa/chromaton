import {
    createGameController,
    getGameByIdController,
    joinGameController,
    startGameController,
    endGameController
} from '../controllers/gamesController.js';
import express from 'express';

const router = express.Router();

router.post('/games/create', createGameController);
router.get('/games/:game_id', getGameByIdController);
router.post('/games/join', joinGameController);
router.post('/games/start', startGameController);
router.post('/games/end', endGameController);

export default router;