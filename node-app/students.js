const { Pool } = require('pg');

function createPool() {
  return new Pool({
    host: 'localhost',
    port: 5432,
    database: 'tp',
    user: 'postgres',
    password: 'postgres',
  });
}

async function addStudent(db, name, grade) {
  const res = await db.query(
    'INSERT INTO students (name, grade) VALUES ($1, $2) RETURNING id',
    [name, grade]
  );
  return res.rows[0].id;
}

async function getStudent(db, name) {
  const res = await db.query(
    'SELECT id, name, grade FROM students WHERE name = $1 ORDER BY id LIMIT 1',
    [name]
  );
  if (res.rows.length === 0) return null;
  const row = res.rows[0];
  return { id: row.id, name: row.name, grade: Number(row.grade) };
}

async function averageGrade(db) {
  const res = await db.query('SELECT AVG(grade) AS avg FROM students');
  const avg = res.rows[0].avg;
  return avg === null ? null : Number(avg);
}

module.exports = { createPool, addStudent, getStudent, averageGrade };
