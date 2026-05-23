package exceptions;

public class PrerequisiteNotMetException extends CRSException {
    public PrerequisiteNotMetException(String missingCourse) {
        super("Prerequisite not completed: " + missingCourse);
    }
}