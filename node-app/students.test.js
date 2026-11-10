const { createPool, addStudent, getStudent, averageGrade } = require('./students');

let db;

beforeAll(() => {
  db = createPool();
});

beforeEach(async () => {
  await db.query('DELETE FROM students');
});

afterAll(async () => {
  await db.end();
});

test('ajouter puis relire un étudiant', async () => {
  await addStudent(db, 'Amel', 15);
  const etudiant = await getStudent(db, 'Amel');
  expect(etudiant.name).toBe('Amel');
  expect(etudiant.grade).toBe(15);
});

test("lire un étudiant qui n'existe pas renvoie null", async () => {
  expect(await getStudent(db, 'Personne')).toBeNull();
});

test('la moyenne de 10 et 14 vaut 12', async () => {
  await addStudent(db, 'Amel', 10);
  await addStudent(db, 'Karim', 14);
  expect(await averageGrade(db)).toBe(12);
});
