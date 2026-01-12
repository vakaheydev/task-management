import aiohttp
from datetime import datetime, timedelta, timezone
from config import CLIENT_USERNAME, CLIENT_PASSWORD, BASE_URL

class TaskManagementApiClient:
    def __init__(self):
        self.username = CLIENT_USERNAME
        self.password = CLIENT_PASSWORD
        self.base_url = BASE_URL
        
        self.token = None
        self.expires_at = None
        self.session = None
        
    async def __aenter__(self):
        self.session = aiohttp.ClientSession()
        return self
    
    async def __aexit__(self, exc_type, exc_val, exc_tb):
        if self.session:
            await self.session.close()
    
    async def authorize_if_needed(self):
        now = datetime.now(timezone.utc)
        
        if self.token is None or (self.expires_at and now >= self.expires_at - timedelta(seconds=30)):
            async with self.session.post(
                f"{self.base_url}/api/auth/login",
                json={"username": self.username, "password": self.password}
            ) as response:
                data = await response.json()
                self.token = data["accessToken"]
                self.expires_at = datetime.fromisoformat(data["expiresAt"])
                
    async def get_user_by_tg_id(self, tg_id):
        return await self.get_request(f"/api/user/search?tgId={tg_id}")
                
    async def get_tasks(self, user_id):
        return await self.get_request(f"/api/user/{user_id}/tasks")
    
    async def get_task_types(self):
        return await self.get_request("/api/task_type")
    
    async def get_request(self, url):
        await self.authorize_if_needed()
        headers = {"Authorization": f"Bearer {self.token}"}
        async with self.session.get(f"{self.base_url}{url}", headers=headers) as response:
            json_response = await response.json()
            if response.status >= 400:
                return None
            return json_response
    
    async def post_request(self, url, body):
        await self.authorize_if_needed()
        headers = {"Authorization": f"Bearer {self.token}"}
        async with self.session.post(f"{self.base_url}{url}", json=body, headers=headers) as response:
            json_response = await response.json()
            if response.status >= 400:
                return None
            return json_response