package com.example.employees.controller;

import com.example.employees.model.Employee;
import com.example.employees.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/v1")
public class EmployeeController {

    @Autowired
    EmployeeRepository repository;

    @GetMapping
    @RequestMapping("/sampleEmployee")
    public Employee getEmployee(){
        return new Employee(1, "Caceres", "Antonio", "Software Engineer", 1000.00f, "Guatemala");
    }

//    @PostMapping("/employee")
//    public ResponseEntity<?> createEmployee(@RequestBody Employee employee){
//        repository.save(employee);
//        return new ResponseEntity<>("Employee was saved successfully", HttpStatus.CREATED);
//    }

//    @GetMapping("/employee/{id}")
//    public Employee getEmployeeById(@PathVariable("id") Integer id){
//
//        return repository.findById(id).orElse(null);
//    }

    @PostMapping("/employee")
    public ResponseEntity<Employee> createEmployee(@RequestBody Employee employee){
        repository.save(employee);
        return new ResponseEntity<>(employee, HttpStatus.CREATED);
    }

    @GetMapping("/employee/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable("id") Integer id){

        return new ResponseEntity<>(repository.findById(id).orElse(null), HttpStatus.OK);
    }

    @GetMapping("/employee/name")
    public Employee getEmployeeByName(
            @RequestParam(name = "lastName", required = false) String lastName,
            @RequestParam(name = "firstName", required = false) String firstName){
        return repository.findEmployeeByName(lastName, firstName);
    }

    @GetMapping("/employee/country")
    public List<Employee> getEmployeesByCountry(
            @RequestParam(name = "country", required = false) String country){
        return repository.findEmployeesByCountry(country);
    }

    @GetMapping("/employees")
    public List<Employee> getAllEmployees(){
        return repository.findAll();
    }

    @GetMapping("/employee/salary")
    public List<Employee> getEmployeesBySalaryRange(
            @RequestParam(name = "startSalary") float startSalary,
            @RequestParam(name = "endSalary") float endSalary){
        return repository.findEmployeesBySalaryRange(startSalary, endSalary);
    }

    @PutMapping("/employee/{id}")
    public ResponseEntity<?> updateEmployee(@PathVariable Integer id, @RequestBody Employee employee){
        //repository.findById(id).ifPresent(emp -> repository.save(employee));
        Employee emp = repository.findById(id).orElse(null);
        if(!Objects.isNull(emp)){
            emp.setSalary(employee.getSalary());
            repository.save(emp);
            return new ResponseEntity<>("Employee was modified successfully", HttpStatus.OK);
        }
        return new ResponseEntity<>("Employee was not found", HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/employee/{id}")
    public ResponseEntity<?> deleteEmployee(@PathVariable Integer id){
        Employee employee = repository.findById(id).orElse(null);
        if(!Objects.isNull(employee)){
            repository.deleteById(id);
            return new ResponseEntity<>("Employee was deleted successfully", HttpStatus.OK);
        }
        return new ResponseEntity<>("Employee was not found", HttpStatus.NOT_FOUND);
    }

    @GetMapping("/employee/page")
    public Page<Employee> getEmployeePage(@RequestParam(name = "pageNo") int pageNo,
                                          @RequestParam(name = "pageSize") int pageSize){
        return repository.findAll(PageRequest.of(pageNo-1, pageSize, Sort.by("id").ascending()));
    }

    /*
    * Pageable sortedByName = PageRequest.of(0, 3, Sort.by("lastName"));

    Pageable sortedByPriceDesc = PageRequest.of(0, 3, Sort.by("salary").descending());

    Pageable sortedByPriceDescNameAsc = PageRequest.of(0, 5, Sort.by("salary").descending().and(Sort.by("id")));
    * */
}
