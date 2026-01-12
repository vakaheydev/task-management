from datetime import datetime

def format_tasks(tasks: list[dict]) -> str:
    if not tasks:
        return "У вас нет задач."
    
    lines = [""]
    for task in tasks:
        task_str = f"- {task.get('name', 'Без названия')}: {task.get('description', 'Нет описания')}"
        if task.get('deadline') and task['deadline'] != "null":
            deadline_dt = datetime.fromisoformat(task['deadline'])
            task_str += f" (Дедлайн: {deadline_dt.strftime('%d.%m.%Y %H:%M')})"
        lines.append(task_str)
    
    return "\n".join(lines)
    