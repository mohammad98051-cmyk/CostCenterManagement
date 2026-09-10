package persistence;
import java.io.*;
import domain.Faculty;
import domain.FacultyDAO;

public class SerializedFacultyDAO implements FacultyDAO {

	@Override
	public Faculty load(String filePath) {

		try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath))) {
			return (Faculty) ois.readObject();
		} catch (IOException | ClassNotFoundException e) {
			// e.printStackTrace();
			System.out.println("Kein gespeicherter Zustand gefunden. Fakultät 'Informationstechnik' wurde angelegt.");
		}

		return null;

	}

	@Override
	public void save(Faculty faculty, String filePath) {
		try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
			oos.writeObject(faculty);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
