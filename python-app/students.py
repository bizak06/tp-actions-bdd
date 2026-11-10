"""Gestion des étudiants dans PostgreSQL (psycopg2)."""
import psycopg2

DB_CONFIG = {
    "host": "localhost",
    "port": 5432,
    "dbname": "tp",
    "user": "postgres",
    "password": "postgres",
}


def connect():
    return psycopg2.connect(**DB_CONFIG)


def add_student(conn, name, grade):
    with conn.cursor() as cur:
        cur.execute(
            "INSERT INTO students (name, grade) VALUES (%s, %s) RETURNING id",
            (name, grade),
        )
        new_id = cur.fetchone()[0]
    conn.commit()
    return new_id


def get_student(conn, name):
    with conn.cursor() as cur:
        cur.execute(
            "SELECT id, name, grade FROM students WHERE name = %s ORDER BY id LIMIT 1",
            (name,),
        )
        row = cur.fetchone()
    if row is None:
        return None
    return {"id": row[0], "name": row[1], "grade": float(row[2])}


def average_grade(conn):
    with conn.cursor() as cur:
        cur.execute("SELECT AVG(grade) FROM students")
        avg = cur.fetchone()[0]
    return None if avg is None else float(avg)
