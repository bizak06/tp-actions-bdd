# TP N°3 : GitHub Actions avec PostgreSQL

Application de gestion d'étudiants (ajouter, lire, moyenne) testée automatiquement
dans GitHub Actions sur **Python, JavaScript, Java et PHP**, contre une vraie base PostgreSQL.

## Structure

```
.github/workflows/   db-check.yml (exercice 1) et ci.yml (exercices 2 à 4)
db/                  init.sql, setup.sh (fournis)
python-app/          students.py, conftest.py, test_students.py, requirements.txt
node-app/            students.js, students.test.js, package.json
java-app/            pom.xml, src/main/java/Students.java, src/test/java/StudentsTest.java
php-app/             Students.php, composer.json, tests/StudentsTest.php
```

## Lancer en local (PostgreSQL installé)

```bash
bash db/setup.sh        # démarre PostgreSQL, crée la base tp et la table
cd python-app && pip install -r requirements.txt && pytest -v
cd ../node-app && npm ci && npm test
cd ../java-app && mvn -B package
cd ../php-app && composer install && vendor/bin/phpunit tests
```

## Pipeline

| Job     | Ce qu'il fait                                      |
|---------|----------------------------------------------------|
| python  | Matrice Python 3.10 et 3.12, tests pytest          |
| node    | Node.js 20, npm ci, Jest                           |
| java    | Java 17, mvn package (JUnit 5)                     |
| php     | PHP 8.3, composer, PHPUnit                         |
| build   | Lancé seulement si les 4 langages sont verts       |
| deploy  | Lancé seulement sur `main`, vérifie le secret DEPLOY_TOKEN |
