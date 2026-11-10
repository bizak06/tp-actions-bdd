"""Fixture partagée : connexion à la base avant chaque test, table vidée."""
import psycopg2
import pytest

from students import DB_CONFIG


@pytest.fixture
def conn():
    connection = psycopg2.connect(**DB_CONFIG)
    with connection.cursor() as cur:
        cur.execute("DELETE FROM students")
    connection.commit()
    yield connection
    connection.close()
