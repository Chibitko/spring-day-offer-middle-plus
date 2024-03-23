package com.onedayoffer.taskdistribution.services;

import com.onedayoffer.taskdistribution.DTO.EmployeeDTO;
import com.onedayoffer.taskdistribution.DTO.TaskDTO;
import org.springframework.stereotype.Component;

import java.util.*;

import static java.util.Comparator.comparing;

@Component
public class TaskDistributorImpl implements TaskDistributor  {
    private static final Comparator<EmployeeDTO> EMPLOYEE_PRIORITY_COMPARATOR
            = comparing(EmployeeDTO::getTotalLeadTime);

    private static final Comparator<TaskDTO> TASK_ORDER_COMPARATOR
            = comparing(TaskDTO::getPriority);

    // TODO рассмотреть модифицированнный алгоритм рюкзака.
    public void distribute(List<EmployeeDTO> employees, List<TaskDTO> tasks) {
        // Предусловие.
        assert employees != null;
        assert tasks != null;

        if (employees.isEmpty() || tasks.isEmpty()) {
            // Некому или нечего распределять.
            return;
        }

        PriorityQueue<EmployeeDTO> employeePriority = new PriorityQueue<>(EMPLOYEE_PRIORITY_COMPARATOR);
        employeePriority.addAll(employees);

        List<TaskDTO> orderedTasks = tasks.stream()
                .sorted(TASK_ORDER_COMPARATOR)
                .toList();

        for (TaskDTO task : orderedTasks) {
            EmployeeDTO employee = employeePriority.poll();

            assert employee != null;

            if (employee.getTotalLeadTime() + task.getLeadTime() > 420)
                continue;

            employee.getTasks().add(task);

            employeePriority.remove(employee);
            employeePriority.add(employee);
        }
    }
}
