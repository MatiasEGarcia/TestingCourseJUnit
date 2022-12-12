package com.luv2code.springmvc.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.luv2code.springmvc.models.ScienceGrade;

@Repository
public interface ScienceGradesDao extends CrudRepository<ScienceGrade, Integer>{

	public Iterable<ScienceGrade> findGradeByStudentId(int id);

	public void deleteByStudentId(int id);
}
