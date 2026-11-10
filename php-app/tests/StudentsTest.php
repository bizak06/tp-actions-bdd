<?php

require_once __DIR__ . '/../vendor/autoload.php';

use PHPUnit\Framework\TestCase;

final class StudentsTest extends TestCase
{
    private static PDO $pdo;
    private Students $students;

    public static function setUpBeforeClass(): void
    {
        self::$pdo = Students::connect();
    }

    protected function setUp(): void
    {
        self::$pdo->exec('DELETE FROM students');
        $this->students = new Students(self::$pdo);
    }

    public function testAjouterEtRelire(): void
    {
        $this->students->addStudent('Amel', 15.0);
        $etudiant = $this->students->getStudent('Amel');
        $this->assertSame('Amel', $etudiant['name']);
        $this->assertSame(15.0, $etudiant['grade']);
    }

    public function testEtudiantInexistant(): void
    {
        $this->assertNull($this->students->getStudent('Personne'));
    }

    public function testMoyenne(): void
    {
        $this->students->addStudent('Amel', 10.0);
        $this->students->addStudent('Karim', 14.0);
        $this->assertSame(12.0, $this->students->averageGrade());
    }
}
