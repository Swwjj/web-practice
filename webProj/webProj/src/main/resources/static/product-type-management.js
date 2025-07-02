// 全局变量
let currentProductTypes = [];
let selectedType = null;
let editingType = null;

// API基础URL
const API_BASE = '/actionmall';

// 页面加载完成后初始化
window.addEventListener('DOMContentLoaded', function() {
    loadProductTypes();
});

// 显示消息
function showMessage(message, type = 'info') {
    const container = document.getElementById('message-container');
    container.innerHTML = `<div class="message ${type}">${message}</div>`;
    setTimeout(() => {
        container.innerHTML = '';
    }, 5000);
}

// 显示操作结果
function showOperationResult(message, type = 'info') {
    const container = document.getElementById('operation-result');
    container.innerHTML = `<div class="message ${type}">${message}</div>`;
    setTimeout(() => {
        container.innerHTML = '';
    }, 5000);
}

// 加载产品类型数据
async function loadProductTypes() {
    try {
        const response = await fetch(`${API_BASE}/mgr/param/findptype.do`);
        const result = await response.json();
        if (result.status === 0) {
            currentProductTypes = result.data;
            renderProductTypeTree(result.data);
            updateParentTypeSelect(result.data);
            showMessage('数据加载成功', 'success');
        } else {
            showMessage('加载失败：' + result.msg, 'error');
        }
    } catch (error) {
        showMessage('网络错误：' + error.message, 'error');
    }
}

// 渲染产品类型树
function renderProductTypeTree(types) {
    const container = document.getElementById('tree-container');
    if (!types || types.length === 0) {
        container.innerHTML = `
            <div class="empty-state">
                <div>📁</div>
                <p>暂无产品类型数据</p>
            </div>
        `;
        return;
    }
    const treeHtml = buildTreeHtml(types, 0);
    container.innerHTML = treeHtml;
}

// 构建树形HTML
function buildTreeHtml(types, parentId) {
    const children = types.filter(type => type.parentId === parentId);
    if (children.length === 0) return '';
    let html = '';
    children.forEach(type => {
        const childTypes = buildTreeHtml(types, type.id);
        html += `
            <div class="tree-item" data-id="${type.id}" onclick="selectType(${type.id})">
                <div class="name">${type.name}</div>
                <div class="actions">
                    <button class="edit-btn" onclick="editType(${type.id}, event)">编辑</button>
                    <button class="delete-btn" onclick="deleteType(${type.id}, event)">删除</button>
                </div>
            </div>
            ${childTypes ? `<div class="children">${childTypes}</div>` : ''}
        `;
    });
    return html;
}

// 选择类型
function selectType(id) {
    document.querySelectorAll('.tree-item').forEach(item => {
        item.classList.remove('selected');
    });
    const selectedItem = document.querySelector(`[data-id="${id}"]`);
    if (selectedItem) {
        selectedItem.classList.add('selected');
    }
    selectedType = currentProductTypes.find(type => type.id === id);
    if (selectedType) {
        document.getElementById('typeName').value = selectedType.name;
        document.getElementById('sortOrder').value = selectedType.sortOrder || 1;
        document.getElementById('parentTypeSelect').value = selectedType.parentId || 0;
    }
}

// 更新父类型选择器
function updateParentTypeSelect(types) {
    const select = document.getElementById('parentTypeSelect');
    select.innerHTML = '<option value="0">顶级分类</option>';
    types.forEach(type => {
        select.innerHTML += `<option value="${type.id}">${type.name}</option>`;
    });
}

// 新增产品类型
async function addProductType() {
    const name = document.getElementById('typeName').value.trim();
    const parentId = document.getElementById('parentTypeSelect').value;
    const sortOrder = document.getElementById('sortOrder').value;
    if (!name) {
        showOperationResult('请输入类型名称', 'error');
        return;
    }
    try {
        const response = await fetch(`${API_BASE}/mgr/param/saveparam.do`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                name: name,
                parentId: parentId,
                sortOrder: sortOrder
            })
        });
        const result = await response.json();
        if (result.status === 0) {
            showOperationResult('新增成功', 'success');
            loadProductTypes();
            document.getElementById('typeName').value = '';
            document.getElementById('sortOrder').value = '1';
        } else {
            showOperationResult('新增失败：' + result.msg, 'error');
        }
    } catch (error) {
        showOperationResult('网络错误：' + error.message, 'error');
    }
}

// 更新产品类型
async function updateProductType() {
    if (!selectedType) {
        showOperationResult('请先选择要更新的类型', 'error');
        return;
    }
    const name = document.getElementById('typeName').value.trim();
    if (!name) {
        showOperationResult('请输入类型名称', 'error');
        return;
    }
    try {
        const response = await fetch(`${API_BASE}/mgr/param/updateparam.do`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                id: selectedType.id,
                name: name
            })
        });
        const result = await response.json();
        if (result.status === 0) {
            showOperationResult('更新成功', 'success');
            loadProductTypes();
        } else {
            showOperationResult('更新失败：' + result.msg, 'error');
        }
    } catch (error) {
        showOperationResult('网络错误：' + error.message, 'error');
    }
}

// 删除产品类型
async function deleteProductType() {
    if (!selectedType) {
        showOperationResult('请先选择要删除的类型', 'error');
        return;
    }
    if (!confirm(`确定要删除类型"${selectedType.name}"吗？`)) {
        return;
    }
    try {
        const response = await fetch(`${API_BASE}/mgr/param/delparam.do`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                id: selectedType.id
            })
        });
        const result = await response.json();
        if (result.status === 0) {
            showOperationResult('删除成功', 'success');
            selectedType = null;
            loadProductTypes();
            document.getElementById('typeName').value = '';
            document.getElementById('sortOrder').value = '1';
        } else {
            showOperationResult('删除失败：' + result.msg, 'error');
        }
    } catch (error) {
        showOperationResult('网络错误：' + error.message, 'error');
    }
}

// 编辑类型（显示模态框）
function editType(id, event) {
    event.stopPropagation();
    editingType = currentProductTypes.find(type => type.id === id);
    if (editingType) {
        document.getElementById('editTypeName').value = editingType.name;
        document.getElementById('editModal').style.display = 'block';
    }
}

// 保存编辑
async function saveEdit() {
    if (!editingType) return;
    const name = document.getElementById('editTypeName').value.trim();
    if (!name) {
        showOperationResult('请输入类型名称', 'error');
        return;
    }
    try {
        const response = await fetch(`${API_BASE}/mgr/param/updateparam.do`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                id: editingType.id,
                name: name
            })
        });
        const result = await response.json();
        if (result.status === 0) {
            showOperationResult('更新成功', 'success');
            closeEditModal();
            loadProductTypes();
        } else {
            showOperationResult('更新失败：' + result.msg, 'error');
        }
    } catch (error) {
        showOperationResult('网络错误：' + error.message, 'error');
    }
}

// 关闭编辑模态框
function closeEditModal() {
    document.getElementById('editModal').style.display = 'none';
    editingType = null;
}

// 删除类型
function deleteType(id, event) {
    event.stopPropagation();
    const type = currentProductTypes.find(t => t.id === id);
    if (type && confirm(`确定要删除类型"${type.name}"吗？`)) {
        deleteProductTypeById(id);
    }
}

// 根据ID删除产品类型
async function deleteProductTypeById(id) {
    try {
        const response = await fetch(`${API_BASE}/mgr/param/delparam.do`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                id: id
            })
        });
        const result = await response.json();
        if (result.status === 0) {
            showOperationResult('删除成功', 'success');
            loadProductTypes();
        } else {
            showOperationResult('删除失败：' + result.msg, 'error');
        }
    } catch (error) {
        showOperationResult('网络错误：' + error.message, 'error');
    }
}

// 显示新增模态框
function showAddModal() {
    document.getElementById('typeName').value = '';
    document.getElementById('sortOrder').value = '1';
    document.getElementById('parentTypeSelect').value = '0';
}

// 获取带路径的参数信息
async function loadPathParams() {
    try {
        const response = await fetch(`${API_BASE}/mgr/param/findpathparam.do`);
        const result = await response.json();
        if (result.status === 0) {
            showOperationResult(`获取带路径参数成功，共${result.data.length}条数据`, 'success');
            console.log('带路径参数数据：', result.data);
        } else {
            showOperationResult('获取失败：' + result.msg, 'error');
        }
    } catch (error) {
        showOperationResult('网络错误：' + error.message, 'error');
    }
}

// 获取产品类型参数
async function loadPType() {
    try {
        const response = await fetch(`${API_BASE}/mgr/param/findptype.do`);
        const result = await response.json();
        if (result.status === 0) {
            showOperationResult(`获取产品类型成功，共${result.data.length}条数据`, 'success');
            console.log('产品类型数据：', result.data);
        } else {
            showOperationResult('获取失败：' + result.msg, 'error');
        }
    } catch (error) {
        showOperationResult('网络错误：' + error.message, 'error');
    }
}

// 获取配件类型参数
async function loadPartsType() {
    if (!selectedType) {
        showOperationResult('请先选择产品类型', 'error');
        return;
    }
    try {
        const response = await fetch(`${API_BASE}/mgr/param/findpartstype.do?productTypeId=${selectedType.id}`);
        const result = await response.json();
        if (result.status === 0) {
            showOperationResult(`获取配件类型成功，共${result.data.length}条数据`, 'success');
            console.log('配件类型数据：', result.data);
        } else {
            showOperationResult('获取失败：' + result.msg, 'error');
        }
    } catch (error) {
        showOperationResult('网络错误：' + error.message, 'error');
    }
}

// 点击模态框外部关闭
window.onclick = function(event) {
    const modal = document.getElementById('editModal');
    if (event.target === modal) {
        closeEditModal();
    }
}; 