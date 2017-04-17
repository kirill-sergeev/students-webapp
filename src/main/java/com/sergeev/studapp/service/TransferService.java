package com.sergeev.studapp.service;

import com.sergeev.studapp.dao.*;
import com.sergeev.studapp.model.*;
import com.sergeev.studapp.mongo.MongoDaoFactory;
import com.sergeev.studapp.postgres.PgDaoFactory;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TransferService {

    private static List<Account> accounts;
    private static List<Course> courses;
    private static List<Discipline> disciplines;
    private static List<Group> groups;
    private static List<Lesson> lessons;
    private static List<Mark> marks;
    private static List<User> users;

    private static void exportFrom(int database) throws PersistentException {
        DaoFactory dao = DaoFactory.getDaoFactory(database);
        AccountDao accountDao = dao.getAccountDao();
        CourseDao courseDao = dao.getCourseDao();
        DisciplineDao disciplineDao = dao.getDisciplineDao();
        GroupDao groupDao = dao.getGroupDao();
        LessonDao lessonDao = dao.getLessonDao();
        MarkDao markDao = dao.getMarkDao();
        UserDao userDao = dao.getUserDao();

        accounts = accountDao.getAll();
        courses = courseDao.getAll();
        disciplines = disciplineDao.getAll();
        groups = groupDao.getAll();
        lessons = lessonDao.getAll();
        marks = markDao.getAll();
        users = userDao.getAll();
    }

    private static void importTo(int database) throws PersistentException, IOException {
        prepareSchema(database);
        DaoFactory dao = DaoFactory.getDaoFactory(database);
        String oldId;
        String newId;
        Map<String, String> accountIDs = new HashMap<>();
        Map<String, String> courseIDs = new HashMap<>();
        Map<String, String> disciplineIDs = new HashMap<>();
        Map<String, String> groupIDs = new HashMap<>();
        Map<String, String> lessonIDs = new HashMap<>();
        Map<String, String> userIDs = new HashMap<>();

        for (Account account : accounts) {
            oldId = account.getId();
            String token = account.getToken();
            account = dao.getAccountDao().persist(account);
            account.setToken(token);
            dao.getAccountDao().update(account);
            newId = account.getId();
            accountIDs.put(oldId, newId);
        }

        for (Group group : groups) {
            oldId = group.getId();
            group = dao.getGroupDao().persist(group);
            newId = group.getId();
            groupIDs.put(oldId, newId);
        }

        for (User user : users) {
            oldId = user.getId();
            user.setAccount(new Account().setId(accountIDs.get(user.getAccount().getId())));
            if (user.getType() == User.Role.STUDENT) {
                user.setGroup(new Group().setId(groupIDs.get(user.getGroup().getId())));
            }
            user = dao.getUserDao().persist(user);
            newId = user.getId();
            userIDs.put(oldId, newId);
        }

        for (Discipline discipline : disciplines) {
            oldId = discipline.getId();
            discipline = dao.getDisciplineDao().persist(discipline);
            newId = discipline.getId();
            disciplineIDs.put(oldId, newId);
        }

        for (Course course : courses) {
            oldId = course.getId();
            course.setDiscipline(new Discipline().setId(disciplineIDs.get(course.getDiscipline().getId())));
            course.setGroup(new Group().setId(groupIDs.get(course.getGroup().getId())));
            course.setTeacher(new User().setId(userIDs.get(course.getTeacher().getId())));
            course = dao.getCourseDao().persist(course);
            newId = course.getId();
            courseIDs.put(oldId, newId);
        }

        for (Lesson lesson : lessons) {
            oldId = lesson.getId();
            lesson.setCourse(new Course().setId(courseIDs.get(lesson.getCourse().getId())));
            lesson = dao.getLessonDao().persist(lesson);
            newId = lesson.getId();
            lessonIDs.put(oldId, newId);
        }

        for (Mark mark : marks) {
            mark.setLesson(new Lesson().setId(lessonIDs.get(mark.getLesson().getId())));
            mark.setStudent(new User().setId(userIDs.get(mark.getStudent().getId())));
            dao.getMarkDao().persist(mark);
        }
    }

    private static void prepareSchema(int database) throws IOException {
        if (database == DaoFactory.POSTGRES) {
            String schema = new String(Files.readAllBytes(Paths.get("pg_schema.sql")), Charset.forName("UTF8"));
            String data = new String(Files.readAllBytes(Paths.get("pg_data_default.sql")), Charset.forName("UTF8"));
            String sql = schema + data;
            try (Connection connection = PgDaoFactory.getConnection();
                 Statement statement = connection.createStatement()) {
                statement.execute(sql);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (database == DaoFactory.MONGO) {
            MongoDaoFactory.getConnection().drop();
        }
    }

    public static void main(String[] args) throws PersistentException, IOException {
        exportFrom(DaoFactory.MONGO);
        importTo(DaoFactory.POSTGRES);
    }
}