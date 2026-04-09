import { Router } from "express";
import { registerUser, getUserByIdController } from "../controllers/usersController.js";

const router = Router();

router.post("/users", registerUser);
router.get("/users/:user_id", getUserByIdController);


export default router;