package com.sergeev.studapp.dao;

import com.sergeev.studapp.model.Mark;

import java.util.List;

public interface MarkDao extends GenericDao<Mark> {

    Double getAvgMark(String studentId, String disciplineId) throws PersistentException;

    List<Mark> getByLesson(String lessonId) throws PersistentException;

    List<Mark> getByDisciplineAndStudent(String disciplineId, String studentId) throws PersistentException;

}
