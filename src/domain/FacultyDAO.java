package domain;

public interface FacultyDAO {
	public Faculty load(String filePath);
	public void save(Faculty faculty, String filePath);
}
