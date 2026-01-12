from task_management_api_client import TaskManagementApiClient


class TaskManagementService:
    def __init__(self, api_client: TaskManagementApiClient):
        self.api_client = api_client

    async def get_user_tasks(self, tg_id):
        user = await self.api_client.get_user_by_tg_id(tg_id)
        if not user:
            return None
        user_id = user["id"]
        tasks = await self.api_client.get_tasks(user_id)
        return tasks