<?php

final class Students
{
    public function __construct(private PDO $pdo) {}

    public static function connect(): PDO
    {
        return new PDO(
            'pgsql:host=localhost;port=5432;dbname=tp',
            'postgres',
            'postgres',
            [PDO::ATTR_ERRMODE => PDO::ERRMODE_EXCEPTION]
        );
    }

    public function addStudent(string $name, float $grade): int
    {
        $stmt = $this->pdo->prepare(
            'INSERT INTO students (name, grade) VALUES (:name, :grade) RETURNING id'
        );
        $stmt->execute(['name' => $name, 'grade' => $grade]);
        return (int) $stmt->fetchColumn();
    }

    public function getStudent(string $name): ?array
    {
        $stmt = $this->pdo->prepare(
            'SELECT id, name, grade FROM students WHERE name = :name ORDER BY id LIMIT 1'
        );
        $stmt->execute(['name' => $name]);
        $row = $stmt->fetch(PDO::FETCH_ASSOC);
        if ($row === false) {
            return null;
        }
        return [
            'id' => (int) $row['id'],
            'name' => $row['name'],
            'grade' => (float) $row['grade'],
        ];
    }

    public function averageGrade(): ?float
    {
        $avg = $this->pdo->query('SELECT AVG(grade) FROM students')->fetchColumn();
        return $avg === null || $avg === false ? null : (float) $avg;
    }
}
