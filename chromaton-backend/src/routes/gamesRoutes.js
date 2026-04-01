import { createGameController, getGameByIdController, joinGameController } from '../controllers/gamesController.js';
import express from 'express';

const router = express.Router();

router.post('/games/create', createGameController);
router.get('/games/:game_id', getGameByIdController);
router.post('/games/join', joinGameController);

export default router;