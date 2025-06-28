<template>
  <div class="product-types">
    <!-- 页面头部 -->
    <div class="page-header">
      <h1>产品类型分类</h1>
      <button class="back-btn" @click="goBack">
        <span>←</span> 返回首页
      </button>
    </div>

    <!-- 加载状态 -->
    <div v-if="loading" class="loading">
      <div class="spinner"></div>
      <p>加载中...</p>
    </div>

    <!-- 错误状态 -->
    <div v-else-if="error" class="error">
      <p>{{ error }}</p>
      <button @click="fetchProductTypes" class="retry-btn">重试</button>
    </div>

    <!-- 产品类型树形结构 -->
    <div v-else class="types-section">
      <div class="types-tree">
        <div 
          v-for="category in productTypes" 
          :key="category.id" 
          class="category-item"
        >
          <!-- 一级分类 -->
          <div class="category-header" @click="toggleCategory(category.id)">
            <div class="category-info">
              <span class="category-name">{{ category.name }}</span>
              <span class="category-count" v-if="category.children.length > 0">
                ({{ category.children.length }}个子分类)
              </span>
            </div>
            <div class="toggle-icon" :class="{ expanded: expandedCategories.includes(category.id) }">
              <span v-if="category.children.length > 0">▼</span>
            </div>
          </div>

          <!-- 二级分类 -->
          <div 
            v-if="category.children.length > 0 && expandedCategories.includes(category.id)"
            class="sub-categories"
          >
            <div 
              v-for="subCategory in category.children" 
              :key="subCategory.id" 
              class="sub-category-item"
            >
              <div class="sub-category-header" @click="toggleSubCategory(subCategory.id)">
                <div class="sub-category-info">
                  <span class="sub-category-name">{{ subCategory.name }}</span>
                  <span class="sub-category-count" v-if="subCategory.children.length > 0">
                    ({{ subCategory.children.length }}个子分类)
                  </span>
                </div>
                <div class="toggle-icon" :class="{ expanded: expandedSubCategories.includes(subCategory.id) }">
                  <span v-if="subCategory.children.length > 0">▼</span>
                </div>
              </div>

              <!-- 三级分类 -->
              <div 
                v-if="subCategory.children.length > 0 && expandedSubCategories.includes(subCategory.id)"
                class="sub-sub-categories"
              >
                <div 
                  v-for="subSubCategory in subCategory.children" 
                  :key="subSubCategory.id" 
                  class="sub-sub-category-item"
                >
                  <div class="sub-sub-category-info">
                    <span class="sub-sub-category-name">{{ subSubCategory.name }}</span>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- 统计信息 -->
      <div class="stats-info">
        <div class="stat-item">
          <span class="stat-label">一级分类：</span>
          <span class="stat-value">{{ productTypes.length }}个</span>
        </div>
        <div class="stat-item">
          <span class="stat-label">总分类数：</span>
          <span class="stat-value">{{ totalCategories }}个</span>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { getProductTypes } from '@/api/productType';

const router = useRouter();

// 响应式数据
const loading = ref(false);
const error = ref('');
const productTypes = ref([]);
const expandedCategories = ref([]);
const expandedSubCategories = ref([]);

// 计算属性
const totalCategories = computed(() => {
  let count = productTypes.value.length;
  productTypes.value.forEach(category => {
    count += category.children.length;
    category.children.forEach(subCategory => {
      count += subCategory.children.length;
    });
  });
  return count;
});

// 获取产品类型数据
const fetchProductTypes = async () => {
  try {
    loading.value = true;
    error.value = '';
    
    const res = await getProductTypes();
    
    if (res.status === 0) {
      productTypes.value = res.data;
      // 默认展开所有一级分类
      expandedCategories.value = res.data.map(item => item.id);
    } else {
      error.value = res.msg || '获取产品类型失败';
    }
  } catch (e) {
    error.value = e.message || '获取产品类型失败';
  } finally {
    loading.value = false;
  }
};

// 切换一级分类展开/收起
const toggleCategory = (categoryId) => {
  const index = expandedCategories.value.indexOf(categoryId);
  if (index > -1) {
    expandedCategories.value.splice(index, 1);
  } else {
    expandedCategories.value.push(categoryId);
  }
};

// 切换二级分类展开/收起
const toggleSubCategory = (subCategoryId) => {
  const index = expandedSubCategories.value.indexOf(subCategoryId);
  if (index > -1) {
    expandedSubCategories.value.splice(index, 1);
  } else {
    expandedSubCategories.value.push(subCategoryId);
  }
};

// 返回首页
const goBack = () => {
  router.push('/');
};

onMounted(() => {
  fetchProductTypes();
});
</script>

<style scoped>
.product-types {
  max-width: 1200px;
  margin: 0 auto;
  padding: 1rem;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 2rem;
  padding-bottom: 1rem;
  border-bottom: 1px solid #eee;
}

.page-header h1 {
  margin: 0;
  color: #2c3e50;
  font-size: 1.8rem;
}

.back-btn {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  padding: 0.5rem 1rem;
  background: #f8f9fa;
  border: 1px solid #dee2e6;
  border-radius: 6px;
  cursor: pointer;
  transition: all 0.3s ease;
}

.back-btn:hover {
  background: #e9ecef;
}

.loading {
  text-align: center;
  padding: 3rem;
}

.spinner {
  width: 40px;
  height: 40px;
  border: 4px solid #f3f3f3;
  border-top: 4px solid #3498db;
  border-radius: 50%;
  animation: spin 1s linear infinite;
  margin: 0 auto 1rem;
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}

.error {
  text-align: center;
  padding: 3rem;
  color: #e74c3c;
}

.retry-btn {
  margin-top: 1rem;
  padding: 0.5rem 1rem;
  background: #3498db;
  color: white;
  border: none;
  border-radius: 6px;
  cursor: pointer;
}

.types-section {
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  overflow: hidden;
}

.types-tree {
  padding: 1rem;
}

.category-item {
  margin-bottom: 1rem;
  border: 1px solid #e9ecef;
  border-radius: 6px;
  overflow: hidden;
}

.category-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 1rem;
  background: #f8f9fa;
  cursor: pointer;
  transition: all 0.3s ease;
}

.category-header:hover {
  background: #e9ecef;
}

.category-info {
  display: flex;
  align-items: center;
  gap: 0.5rem;
}

.category-name {
  font-weight: 600;
  color: #2c3e50;
  font-size: 1.1rem;
}

.category-count {
  color: #6c757d;
  font-size: 0.9rem;
}

.toggle-icon {
  transition: transform 0.3s ease;
}

.toggle-icon.expanded {
  transform: rotate(180deg);
}

.sub-categories {
  border-top: 1px solid #e9ecef;
}

.sub-category-item {
  border-bottom: 1px solid #f1f3f4;
}

.sub-category-item:last-child {
  border-bottom: none;
}

.sub-category-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0.75rem 1rem 0.75rem 2rem;
  background: white;
  cursor: pointer;
  transition: all 0.3s ease;
}

.sub-category-header:hover {
  background: #f8f9fa;
}

.sub-category-info {
  display: flex;
  align-items: center;
  gap: 0.5rem;
}

.sub-category-name {
  font-weight: 500;
  color: #495057;
}

.sub-category-count {
  color: #6c757d;
  font-size: 0.85rem;
}

.sub-sub-categories {
  background: #f8f9fa;
  border-top: 1px solid #e9ecef;
}

.sub-sub-category-item {
  padding: 0.5rem 1rem 0.5rem 3rem;
  border-bottom: 1px solid #e9ecef;
}

.sub-sub-category-item:last-child {
  border-bottom: none;
}

.sub-sub-category-info {
  display: flex;
  align-items: center;
}

.sub-sub-category-name {
  color: #6c757d;
  font-size: 0.9rem;
}

.stats-info {
  display: flex;
  justify-content: space-around;
  padding: 1rem;
  background: #f8f9fa;
  border-top: 1px solid #e9ecef;
}

.stat-item {
  display: flex;
  align-items: center;
  gap: 0.5rem;
}

.stat-label {
  color: #6c757d;
  font-size: 0.9rem;
}

.stat-value {
  font-weight: 600;
  color: #2c3e50;
  font-size: 1.1rem;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .page-header {
    flex-direction: column;
    gap: 1rem;
    text-align: center;
  }
  
  .stats-info {
    flex-direction: column;
    gap: 0.5rem;
    text-align: center;
  }
  
  .category-header,
  .sub-category-header {
    padding: 0.75rem;
  }
  
  .sub-category-header {
    padding-left: 1.5rem;
  }
  
  .sub-sub-category-item {
    padding-left: 2rem;
  }
}
</style> 