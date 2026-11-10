from students import add_student, get_student, average_grade


def test_ajouter_et_lire(conn):
    add_student(conn, "Amel", 15)
    etudiant = get_student(conn, "Amel")
    assert etudiant["name"] == "Amel"
    assert etudiant["grade"] == 15.0


def test_etudiant_inexistant(conn):
    assert get_student(conn, "Personne") is None


def test_moyenne(conn):
    add_student(conn, "Amel", 10)
    add_student(conn, "Karim", 14)
    assert average_grade(conn) == 12.0
