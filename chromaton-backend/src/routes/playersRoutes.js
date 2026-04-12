import {  createPlayerController, getPlayersByGameIdController } from "../controllers/playersController";
import express from 'express';

const router = express.Router();

router.get('/players/game/:game_id', getPlayersByGameIdController);

export default router;