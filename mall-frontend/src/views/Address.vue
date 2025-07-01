<template>
  <div class="address-page">
    <div class="address-header">
      <el-button @click="$router.push('/')" type="primary" plain>
        <el-icon><ArrowLeft /></el-icon>
        返回首页
      </el-button>
      <h2>收货地址管理</h2>
      <el-button type="primary" @click="handleAddAddress">
        <el-icon><Plus /></el-icon>
        新增地址
      </el-button>
    </div>

    <div class="address-content">
      <!-- 加载状态 -->
      <div v-if="loading" class="loading-container">
        <el-skeleton :rows="3" animated />
      </div>

      <!-- 错误状态 -->
      <div v-else-if="error" class="error-container">
        <el-result
          icon="error"
          :title="error"
          sub-title="请检查网络连接或重新登录"
        >
          <template #extra>
            <el-button type="primary" @click="fetchAddressList">重新加载</el-button>
          </template>
        </el-result>
      </div>

      <!-- 空状态 -->
      <div v-else-if="addressList.length === 0" class="empty-container">
        <el-empty description="暂无收货地址">
          <el-button type="primary" @click="handleAddAddress">添加地址</el-button>
        </el-empty>
      </div>

      <!-- 地址列表 -->
      <div v-else class="address-list">
        <el-card 
          v-for="address in addressList" 
          :key="address.id" 
          class="address-item"
          :class="{ 'default-address': address.dfault }"
        >
          <div class="address-header-row">
            <div class="contact-info">
              <span class="name">{{ address.name }}</span>
              <span class="phone">{{ address.mobile || address.phone || '暂无手机号' }}</span>
              <el-tag v-if="address.dfault" type="success" size="small">默认地址</el-tag>
            </div>
            <div class="address-actions">
              <el-button 
                v-if="!address.dfault" 
                type="primary" 
                size="small" 
                @click="handleSetDefault(address.id)"
              >
                设为默认
              </el-button>
              <el-button 
                type="primary" 
                size="small" 
                plain
                @click="handleEditAddress(address)"
              >
                编辑
              </el-button>
              <el-button 
                type="danger" 
                size="small" 
                @click="handleDeleteAddress(address.id)"
              >
                删除
              </el-button>
            </div>
          </div>
          
          <div class="address-detail">
            <div class="location">
              {{ address.province }} {{ address.city }} {{ address.district }}
            </div>
            <div class="street">
              {{ address.addr }}
            </div>
            <div v-if="address.zip" class="zip">
              邮编：{{ address.zip }}
            </div>
          </div>

          <div class="address-footer">
            <span class="create-time">
              创建时间：{{ formatTime(address.created) }}
            </span>
            <span v-if="address.updated !== address.created" class="update-time">
              更新时间：{{ formatTime(address.updated) }}
            </span>
          </div>
        </el-card>
      </div>
    </div>

    <!-- 地址编辑对话框 -->
    <el-dialog 
      v-model="dialogVisible" 
      :title="isEdit ? '编辑地址' : '新增地址'"
      width="600px"
      :close-on-click-modal="false"
      :close-on-press-escape="false"
    >
      <el-form
        ref="formRef"
        :model="formData"
        :rules="rules"
        label-width="100px"
        @submit.prevent
      >
        <el-form-item label="收货人" prop="name">
          <el-input 
            v-model="formData.name" 
            placeholder="请输入收货人姓名"
            clearable
          />
        </el-form-item>

        <el-form-item label="手机号码" prop="mobile">
          <el-input 
            v-model="formData.mobile" 
            placeholder="请输入手机号码"
            clearable
          />
        </el-form-item>

        <el-form-item label="所在地区" prop="region">
          <el-cascader
            v-model="formData.region"
            :options="regionOptions"
            placeholder="请选择所在地区"
            :props="cascaderProps"
            @change="handleRegionChange"
            clearable
          />
        </el-form-item>

        <el-form-item label="详细地址" prop="addr">
          <el-input 
            v-model="formData.addr" 
            type="textarea"
            :rows="3"
            placeholder="请输入详细地址"
            clearable
          />
        </el-form-item>

        <el-form-item label="邮政编码" prop="zip">
          <el-input 
            v-model="formData.zip" 
            placeholder="请输入邮政编码"
            clearable
          />
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="handleSubmitAddress" :loading="submitting">
            {{ isEdit ? '保存修改' : '保存地址' }}
          </el-button>
          <el-button @click="handleCancelAddress">取消</el-button>
        </el-form-item>
      </el-form>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { ArrowLeft, Plus } from '@element-plus/icons-vue'
import { getAddressList, addAddress, getAddressById, updateAddress, deleteAddress, setDefaultAddress } from '@/api/address'

// 响应式数据
const loading = ref(false)
const error = ref('')
const addressList = ref([])

// 对话框相关
const dialogVisible = ref(false)
const isEdit = ref(false)
const currentAddress = ref({})
const formRef = ref()
const submitting = ref(false)

// 表单数据 - 严格按照接口文档的字段
const formData = reactive({
  name: '',
  mobile: '',
  region: [],
  province: '',
  city: '',
  district: '',
  addr: '',
  zip: ''
})

// 表单验证规则 - 严格按照接口文档的必需字段
const rules = {
  name: [
    { required: true, message: '请输入收货人姓名', trigger: 'blur' }
  ],
  mobile: [
    { required: true, message: '请输入手机号码', trigger: 'blur' }
  ],
  region: [
    { required: true, message: '请选择所在地区', trigger: 'change' }
  ],
  addr: [
    { required: true, message: '请输入详细地址', trigger: 'blur' }
  ],
  zip: [
    { required: true, message: '请输入邮政编码', trigger: 'blur' }
  ]
}

// 地区选择器配置
const cascaderProps = {
  value: 'name',
  label: 'name',
  children: 'children'
}

// 地区数据（简化版，实际项目中可以从API获取）
const regionOptions = ref([
  {
    name: '山东省',
    children: [
      {
        name: '烟台市',
        children: [
          { name: '芝罘区' },
          { name: '福山区' },
          { name: '牟平区' },
          { name: '莱山区' }
        ]
      },
      {
        name: '青岛市',
        children: [
          { name: '市南区' },
          { name: '市北区' },
          { name: '李沧区' },
          { name: '崂山区' }
        ]
      },
      {
        name: '济南市',
        children: [
          { name: '历下区' },
          { name: '市中区' },
          { name: '槐荫区' },
          { name: '天桥区' }
        ]
      }
    ]
  },
  {
    name: '江苏省',
    children: [
      {
        name: '南京市',
        children: [
          { name: '鼓楼区' },
          { name: '玄武区' },
          { name: '秦淮区' },
          { name: '建邺区' }
        ]
      },
      {
        name: '常州市',
        children: [
          { name: '钟楼区' },
          { name: '天宁区' },
          { name: '新北区' },
          { name: '武进区' }
        ]
      }
    ]
  },
  {
    name: '河北省',
    children: [
      {
        name: '唐山市',
        children: [
          { name: '路北区' },
          { name: '路南区' },
          { name: '开平区' },
          { name: '古冶区' }
        ]
      }
    ]
  }
])

// 获取地址列表
const fetchAddressList = async () => {
  loading.value = true
  error.value = ''
  
  try {
    const response = await getAddressList()
    
    if (response.status === 0) {
      addressList.value = response.data
    } else {
      error.value = response.msg || '获取地址列表失败'
      if (response.msg === '请登录后在进行操作！') {
        ElMessage.error('请先登录')
        // 可以在这里跳转到登录页
      }
    }
  } catch (err) {
    console.error('获取地址列表失败:', err)
    error.value = '网络错误，请稍后重试'
  } finally {
    loading.value = false
  }
}

// 格式化时间
const formatTime = (timestamp) => {
  if (!timestamp) return ''
  return new Date(timestamp).toLocaleString('zh-CN')
}

// 地区选择变化处理
const handleRegionChange = (value) => {
  if (value && value.length >= 3) {
    formData.province = value[0]
    formData.city = value[1]
    formData.district = value[2]
  } else {
    formData.province = ''
    formData.city = ''
    formData.district = ''
  }
}

// 初始化表单数据
const initFormData = () => {
  if (currentAddress.value && Object.keys(currentAddress.value).length > 0) {
    // 编辑模式，填充现有数据
    formData.name = currentAddress.value.name || ''
    formData.mobile = currentAddress.value.mobile || ''
    formData.addr = currentAddress.value.addr || ''
    formData.zip = currentAddress.value.zip || ''
    
    // 设置地区选择器
    if (currentAddress.value.province && currentAddress.value.city && currentAddress.value.district) {
      formData.region = [currentAddress.value.province, currentAddress.value.city, currentAddress.value.district]
      formData.province = currentAddress.value.province
      formData.city = currentAddress.value.city
      formData.district = currentAddress.value.district
    }
  } else {
    // 新增模式，重置表单
    Object.assign(formData, {
      name: '',
      mobile: '',
      region: [],
      province: '',
      city: '',
      district: '',
      addr: '',
      zip: ''
    })
  }
}

// 新增地址
const handleAddAddress = () => {
  isEdit.value = false
  currentAddress.value = {}
  initFormData()
  dialogVisible.value = true
}

// 编辑地址
const handleEditAddress = async (address) => {
  try {
    // 获取地址详情
    const response = await getAddressById(address.id)
    
    if (response.status === 0) {
      isEdit.value = true
      currentAddress.value = response.data
      initFormData()
      dialogVisible.value = true
    } else {
      ElMessage.error(response.msg || '获取地址详情失败')
    }
  } catch (err) {
    console.error('获取地址详情失败:', err)
    ElMessage.error('获取地址详情失败')
  }
}

// 删除地址
const handleDeleteAddress = async (addressId) => {
  try {
    await ElMessageBox.confirm(
      '确定要删除这个地址吗？删除后无法恢复。',
      '确认删除',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    
    // 调用删除地址API
    const response = await deleteAddress(addressId)
    
    if (response.status === 0) {
      ElMessage.success('删除成功')
      
      // 使用返回的地址列表更新本地数据
      if (response.data && response.data.length >= 0) {
        // 将返回的数据转换为UI期望的格式
        const convertedData = response.data.map(addr => ({
          ...addr,
          dfault: addr.default_addr === 1, // 将 default_addr (数字) 转换为 dfault (布尔值)
        }));
        addressList.value = convertedData;
      }
    } else {
      ElMessage.error(response.msg || '删除失败')
      if (response.msg === '请登录后在进行操作！') {
        ElMessage.error('请先登录')
        // 可以在这里跳转到登录页
      }
    }
  } catch (err) {
    if (err !== 'cancel') { // 用户取消删除时不显示错误
      console.error('删除地址失败:', err)
      ElMessage.error('删除失败，请稍后重试')
    }
  }
}

// 设置默认地址
const handleSetDefault = async (addressId) => {
  try {
    // 调用设置默认地址API
    const response = await setDefaultAddress(addressId)
    if (response.status === 0) {
      ElMessage.success('设置默认地址成功')
      // 使用返回的地址列表更新本地数据
      if (response.data && response.data.length >= 0) {
        // 将返回的数据转换为UI期望的格式
        const convertedData = response.data.map(addr => ({
          ...addr,
          dfault: addr.default_addr === 1, // 将 default_addr (数字) 转换为 dfault (布尔值)
        }));
        addressList.value = convertedData;
      }
    } else {
      ElMessage.error(response.msg || '设置默认地址失败')
      if (response.msg === '请登录后在进行操作！') {
        ElMessage.error('请先登录')
        // 可以在这里跳转到登录页
      }
    }
  } catch (err) {
    console.error('设置默认地址失败:', err)
    ElMessage.error('设置默认地址失败，请稍后重试')
  }
}

// 提交地址表单
const handleSubmitAddress = async () => {
  try {
    await formRef.value.validate()
    
    submitting.value = true
    
    // 构建提交数据 - 严格按照接口文档的字段
    const submitData = {
      name: formData.name.trim(),
      mobile: formData.mobile.trim(),
      province: formData.province,
      city: formData.city,
      district: formData.district,
      addr: formData.addr.trim(),
      zip: formData.zip.trim()
    }
    
    let response;
    
    if (isEdit.value) {
      // 编辑模式：调用更新接口
      submitData.id = currentAddress.value.id;
      response = await updateAddress(submitData);
    } else {
      // 新增模式：调用新增接口
      response = await addAddress(submitData);
    }
    
    if (response.status === 0) {
      ElMessage.success(isEdit.value ? '编辑成功' : '新增成功')
      dialogVisible.value = false
      
      if (isEdit.value) {
        // 编辑模式：更新本地数据
        const index = addressList.value.findIndex(addr => addr.id === currentAddress.value.id);
        if (index !== -1) {
          // 将返回的数据转换为UI期望的格式
          const updatedAddress = {
            ...response.data,
            dfault: response.data.default_addr === 1, // 将 default_addr (数字) 转换为 dfault (布尔值)
          };
          addressList.value[index] = updatedAddress;
        }
      } else {
        // 新增模式：使用返回的完整地址列表
        if (response.data && response.data.length > 0) {
          // 将返回的数据转换为UI期望的格式
          const convertedData = response.data.map(addr => ({
            ...addr,
            dfault: addr.default_addr === 1, // 将 default_addr (数字) 转换为 dfault (布尔值)
          }));
          addressList.value = convertedData;
        }
      }
    } else {
      ElMessage.error(response.msg || (isEdit.value ? '编辑失败' : '新增失败'))
    }
  } catch (err) {
    console.error('提交地址失败:', err)
    ElMessage.error('操作失败，请稍后重试')
  } finally {
    submitting.value = false
  }
}

// 取消地址操作
const handleCancelAddress = () => {
  dialogVisible.value = false
}

// 页面加载时获取地址列表
onMounted(() => {
  fetchAddressList()
})
</script>

<style scoped>
.address-page {
  max-width: 800px;
  margin: 0 auto;
  padding: 20px;
}

.address-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
  padding-bottom: 16px;
  border-bottom: 1px solid #ebeef5;
}

.address-header h2 {
  margin: 0;
  color: #303133;
  font-size: 24px;
  font-weight: 600;
}

.address-content {
  min-height: 400px;
}

.loading-container {
  padding: 40px 0;
}

.error-container {
  padding: 40px 0;
}

.empty-container {
  padding: 60px 0;
}

.address-list {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.address-item {
  border: 1px solid #e4e7ed;
  transition: all 0.3s ease;
}

.address-item:hover {
  border-color: #409eff;
  box-shadow: 0 2px 12px rgba(64, 158, 255, 0.1);
}

.address-item.default-address {
  border-color: #67c23a;
  background-color: #f0f9ff;
}

.address-header-row {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 12px;
}

.contact-info {
  display: flex;
  align-items: center;
  gap: 12px;
  flex: 1;
}

.contact-info .name {
  font-size: 16px;
  font-weight: 600;
  color: #303133;
}

.contact-info .phone {
  color: #606266;
  font-size: 14px;
}

.address-actions {
  display: flex;
  gap: 8px;
  flex-shrink: 0;
}

.address-detail {
  margin-bottom: 12px;
  line-height: 1.6;
}

.address-detail .location {
  color: #303133;
  font-size: 14px;
  margin-bottom: 4px;
}

.address-detail .street {
  color: #606266;
  font-size: 14px;
  margin-bottom: 4px;
}

.address-detail .zip {
  color: #909399;
  font-size: 12px;
}

.address-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding-top: 12px;
  border-top: 1px solid #f0f0f0;
  font-size: 12px;
  color: #909399;
}

.create-time,
.update-time {
  font-size: 12px;
}

.el-cascader {
  width: 100%;
}

.el-form-item {
  margin-bottom: 20px;
}
</style> 