import {
    createGameController,
    getGameByIdController,
    joinGameController,
    startGameController,
    endGameController
} from '../controllers/gamesController.js';
import express from 'express';
import { verificarFirebaseToken } from '../middleware/authMiddleware.js';

const router = express.Router();

router.post('/games/create', verificarFirebaseToken, createGameController);
router.get('/games/:game_id', verificarFirebaseToken, getGameByIdController);
router.post('/games/join', verificarFirebaseToken, joinGameController);
router.post('/games/:game_id/start', verificarFirebaseToken, startGameController);
router.post('/games/:game_id/end', verificarFirebaseToken, endGameController);

export default router;