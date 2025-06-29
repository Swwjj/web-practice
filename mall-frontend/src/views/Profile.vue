<script setup>
import { onMounted, ref } from 'vue';
import { useUserStore } from '@/stores/user';
import { useRouter } from 'vue-router';

const userStore = useUserStore();
const loading = ref(true);
const errorMsg = ref('');
const router = useRouter();
const goHome = () => {
  router.push('/');
};

const isEditing = ref(false);
const editForm = ref({});
const formMsg = ref('');
const formSuccess = ref(false);

// 密码修改弹窗相关
const showPwdDialog = ref(false);
const pwdForm = ref({ oldpwd: '', newpwd: '' });
const pwdMsg = ref('');
const pwdSuccess = ref(false);

onMounted(async () => {
  loading.value = true;
  errorMsg.value = '';
  try {
    await userStore.fetchUserInfo();
  } catch (e) {
    errorMsg.value = e.message || '获取用户信息失败';
  } finally {
    loading.value = false;
  }
});

const startEdit = () => {
  isEditing.value = true;
  formMsg.value = '';
  formSuccess.value = false;
  editForm.value = {
    email: userStore.userInfo.email,
    phone: userStore.userInfo.phone,
    question: userStore.userInfo.question,
    asw: userStore.userInfo.asw,
    age: userStore.userInfo.age,
    sex: userStore.userInfo.sex,
  };
};

const cancelEdit = () => {
  isEditing.value = false;
  formMsg.value = '';
  formSuccess.value = false;
};

const handleSave = async () => {
  formMsg.value = '';
  formSuccess.value = false;
  try {
    // 创建要发送的数据副本，并转换性别字段
    const dataToSend = {
      ...editForm.value,
      sex: editForm.value.sex === 1 ? '男' : '女'
    };
    
    await userStore.updateUserInfo(dataToSend);
    formMsg.value = '保存成功！';
    formSuccess.value = true;
    isEditing.value = false;
  } catch (e) {
    formMsg.value = e.message || '保存失败';
    formSuccess.value = false;
  }
};

// 密码修改相关
const handlePwdChange = async () => {
  pwdMsg.value = '';
  pwdSuccess.value = false;
  if (!pwdForm.value.oldpwd || !pwdForm.value.newpwd) {
    pwdMsg.value = '请填写完整';
    return;
  }
  try {
    const res = await userStore.updatePassword({ ...pwdForm.value });
    pwdMsg.value = res.msg || '修改成功';
    pwdSuccess.value = true;
    pwdForm.value.oldpwd = '';
    pwdForm.value.newpwd = '';
    setTimeout(() => {
      closePwdDialog();
    }, 1200);
  } catch (e) {
    pwdMsg.value = e.message || '修改失败';
    pwdSuccess.value = false;
  }
};
const closePwdDialog = () => {
  showPwdDialog.value = false;
  pwdMsg.value = '';
  pwdSuccess.value = false;
  pwdForm.value.oldpwd = '';
  pwdForm.value.newpwd = '';
};
</script>

<template>
  <div class="profile-container">
    <div class="profile-card">
      <div class="profile-header">
        <button class="back-btn" @click="goHome">← 返回首页</button>
        <h2>个人中心</h2>
      </div>
      <div v-if="loading">加载中...</div>
      <div v-else-if="errorMsg" class="error">{{ errorMsg }}</div>
      <div v-else-if="userStore.userInfo">
        <template v-if="!isEditing">
          <ul class="user-info-list">
            <li><strong>用户名：</strong>{{ userStore.userInfo.account }}</li>
            <li><strong>姓名：</strong>{{ userStore.userInfo.name }}</li>
            <li><strong>邮箱：</strong>{{ userStore.userInfo.email }}</li>
            <li><strong>手机号：</strong>{{ userStore.userInfo.phone }}</li>
            <li><strong>年龄：</strong>{{ userStore.userInfo.age }}</li>
            <li><strong>性别：</strong>{{ 
              userStore.userInfo.sex === 1 || userStore.userInfo.sex === true ? '男' : 
              userStore.userInfo.sex === 0 || userStore.userInfo.sex === false ? '女' : '未知' 
            }}</li>
            <li><strong>密保问题：</strong>{{ userStore.userInfo.question }}</li>
            <li><strong>密保答案：</strong>{{ userStore.userInfo.asw }}</li>
            <li><strong>注册时间：</strong>{{ new Date(userStore.userInfo.create_time).toLocaleString() }}</li>
            <li><strong>角色：</strong>{{ userStore.userInfo.role === 1 ? '管理员' : '普通用户' }}</li>
          </ul>
          <div class="profile-actions">
            <button class="edit-btn" @click="startEdit">编辑信息</button>
            <button class="password-btn" @click="showPwdDialog = true">修改密码</button>
          </div>
        </template>
        <template v-else>
          <form class="edit-form" @submit.prevent="handleSave">
            <div class="form-row">
              <label>邮箱：</label>
              <input v-model="editForm.email" type="email" required />
            </div>
            <div class="form-row">
              <label>手机号：</label>
              <input v-model="editForm.phone" type="tel" required />
            </div>
            <div class="form-row">
              <label>年龄：</label>
              <input v-model="editForm.age" type="number" min="0" required />
            </div>
            <div class="form-row">
              <label>性别：</label>
              <select v-model="editForm.sex" required>
                <option :value="1">男</option>
                <option :value="0">女</option>
              </select>
            </div>
            <div class="form-row">
              <label>密保问题：</label>
              <input v-model="editForm.question" type="text" required />
            </div>
            <div class="form-row">
              <label>密保答案：</label>
              <input v-model="editForm.asw" type="text" required />
            </div>
            <div class="form-actions">
              <button type="submit" class="save-btn">保存</button>
              <button type="button" class="cancel-btn" @click="cancelEdit">取消</button>
            </div>
            <div v-if="formMsg" :class="{'success-msg': formSuccess, 'error-msg': !formSuccess}">{{ formMsg }}</div>
          </form>
        </template>
      </div>
      <!-- 密码修改弹窗 -->
      <div v-if="showPwdDialog" class="dialog-mask">
        <div class="dialog-box">
          <h3>修改密码</h3>
          <form @submit.prevent="handlePwdChange">
            <div class="form-row">
              <label>原密码：</label>
              <input v-model="pwdForm.oldpwd" type="password" required />
            </div>
            <div class="form-row">
              <label>新密码：</label>
              <input v-model="pwdForm.newpwd" type="password" required />
            </div>
            <div class="form-actions">
              <button type="submit" class="save-btn">保存</button>
              <button type="button" class="cancel-btn" @click="closePwdDialog">取消</button>
            </div>
            <div v-if="pwdMsg" :class="{'success-msg': pwdSuccess, 'error-msg': !pwdSuccess}">{{ pwdMsg }}</div>
          </form>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.profile-container {
  max-width: 500px;
  margin: 40px auto;
  background: #f7f8fa; /* 更淡的灰色背景 */
  border-radius: 12px;
  box-shadow: 0 4px 16px rgba(0,0,0,0.08);
  padding: 0;
}

.profile-card {
  background: #fff;
  border-radius: 12px;
  margin: 0 auto;
  padding: 32px 24px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.04);
}

.profile-header {
  display: flex;
  align-items: center;
  margin-bottom: 16px;
}

.back-btn {
  padding: 8px 24px;
  background: #409EFF;
  color: #fff;
  border: none;
  border-radius: 4px;
  font-size: 15px;
  cursor: pointer;
  margin-right: 12px;
  transition: background 0.2s;
}

.back-btn:hover {
  background: #66b1ff;
}

h2 {
  text-align: center;
  margin-bottom: 24px;
  color: #222;
  font-weight: 600;
}

.user-info-list {
  list-style: none;
  padding: 0;
  color: #333;
}

.user-info-list li {
  margin-bottom: 16px;
  font-size: 16px;
  color: #333;
}

.user-info-list strong {
  color: #409EFF;
  font-weight: 500;
}

.profile-actions {
  display: flex;
  justify-content: center;
  gap: 18px;
  margin-top: 24px;
}

.edit-btn {
  padding: 8px 32px;
  background: #409EFF;
  color: #fff;
  border: none;
  border-radius: 4px;
  font-size: 15px;
  cursor: pointer;
  transition: background 0.2s;
}

.edit-btn:hover {
  background: #66b1ff;
}

.password-btn {
  padding: 8px 32px;
  background: #e6a23c;
  color: #fff;
  border: none;
  border-radius: 4px;
  font-size: 15px;
  cursor: pointer;
  transition: background 0.2s;
}

.password-btn:hover {
  background: #f3d19e;
  color: #222;
}

.edit-form {
  margin-top: 10px;
}

.form-row {
  display: flex;
  align-items: center;
  margin-bottom: 18px;
}

.form-row label {
  width: 90px;
  color: #555;
  font-size: 15px;
}

.form-row input,
.form-row select {
  flex: 1;
  padding: 8px;
  border: 1px solid #ccc;
  border-radius: 4px;
  font-size: 15px;
}

.form-actions {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
  margin-top: 10px;
}

.save-btn {
  background: #67c23a;
  color: #fff;
  border: none;
  border-radius: 4px;
  padding: 8px 24px;
  font-size: 15px;
  cursor: pointer;
}

.save-btn:hover {
  background: #85ce61;
}

.cancel-btn {
  background: #f56c6c;
  color: #fff;
  border: none;
  border-radius: 4px;
  padding: 8px 24px;
  font-size: 15px;
  cursor: pointer;
}

.cancel-btn:hover {
  background: #ff7875;
}

.success-msg {
  color: #67c23a;
  margin-top: 12px;
  text-align: center;
}

.error-msg {
  color: #f56c6c;
  margin-top: 12px;
  text-align: center;
}

.error {
  color: #f56c6c;
  text-align: center;
}

/* 密码弹窗样式 */
.dialog-mask {
  position: fixed;
  left: 0; top: 0; right: 0; bottom: 0;
  background: rgba(0,0,0,0.18);
  z-index: 1000;
  display: flex;
  align-items: center;
  justify-content: center;
}

.dialog-box {
  background: #fff;
  border-radius: 10px;
  box-shadow: 0 2px 16px rgba(0,0,0,0.13);
  padding: 32px 28px 18px 28px;
  min-width: 320px;
  max-width: 90vw;
}

.dialog-box h3 {
  margin: 0 0 18px 0;
  text-align: center;
  color: #222;
}
</style> 