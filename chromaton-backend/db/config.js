import pg from 'pg';
import dotenv from 'dotenv';

dotenv.config();

const env_host = process.env.DB_HOST;
const env_user = process.env.USER;
const env_password = process.env.DB_PASSWORD;
const env_database = process.env.DB_NAME;

const pool = new pg.Pool({
  connectionString: process.env.DB_URL,
  ssl: {
    rejectUnauthorized: false
  },
  allowExitOnIdle: true
});



export default pool;