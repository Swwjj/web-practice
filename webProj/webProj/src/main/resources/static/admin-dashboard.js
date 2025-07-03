// 页面加载时检查登录状态
window.onload = function() {
    checkLoginStatus();
    loadDashboardStats();
};

// 全局变量存储商品类型ID到名称的映射
let productTypeMap = new Map();
// 全局变量存储父类型ID到子类型列表的映射
let parentChildTypeMap = new Map();

function checkLoginStatus() {
    fetch('http://localhost:8080/actionmall/user/get_user_info.do', {
        credentials: 'include' // 必须
    })
    .then(response => response.json())
    .then(data => {
        if (data.status != 0) {
            // 未登录，跳转到登录页
            window.location.href = 'admin-login.html';
        } else {
            // 显示管理员信息
            document.getElementById('adminName').textContent = data.data.account || '管理员';
        }
    })
    .catch(error => {
        console.error('检查登录状态失败:', error);
        window.location.href = 'admin-login.html';
    });
}

        function loadDashboardStats() {
            // 加载用户统计
            fetch('http://localhost:8080/actionmall/user/finduserlist.do', {
                method: 'POST'
            })
            .then(response => response.json())
            .then(data => {
                if (data.status === 0) {
                    document.getElementById('totalUsers').textContent = data.data.length;
                }
            })
            .catch(error => {
                console.error('加载用户统计失败:', error);
            });

            // 加载订单统计
            fetch('http://localhost:8080/actionmall/mgr/order/findorders_nopages.do')
            .then(response => response.json())
            .then(data => {
                if (data.status === 0) {
                    document.getElementById('totalOrders').textContent = data.data.length;
                    
                    // 计算今日订单数
                    const today = new Date().toDateString();
                    const todayOrders = data.data.filter(order => {
                        const orderDate = new Date(order.created).toDateString();
                        return orderDate === today;
                    });
                    document.getElementById('todayOrders').textContent = todayOrders.length;
                }
            })
            .catch(error => {
                console.error('加载订单统计失败:', error);
            });
        }

function showDashboard() {
    document.getElementById('dashboard-content').classList.remove('hidden');
    document.getElementById('user-content').classList.add('hidden');
    document.getElementById('order-content').classList.add('hidden');
    document.getElementById('product-content').classList.add('hidden');
    document.getElementById('type-content').classList.add('hidden');
    
    // 更新导航状态
    updateNavActive('dashboard');
}

function showUserManagement() {
    document.getElementById('dashboard-content').classList.add('hidden');
    document.getElementById('user-content').classList.remove('hidden');
    document.getElementById('order-content').classList.add('hidden');
    document.getElementById('product-content').classList.add('hidden');
    document.getElementById('type-content').classList.add('hidden');
    
    // 更新导航状态
    updateNavActive('user');
}

function showOrderManagement() {
    document.getElementById('dashboard-content').classList.add('hidden');
    document.getElementById('user-content').classList.add('hidden');
    document.getElementById('order-content').classList.remove('hidden');
    document.getElementById('product-content').classList.add('hidden');
    document.getElementById('type-content').classList.add('hidden');
    
    // 更新导航状态
    updateNavActive('order');
}

function showProductManagement() {
    document.getElementById('dashboard-content').classList.add('hidden');
    document.getElementById('user-content').classList.add('hidden');
    document.getElementById('order-content').classList.add('hidden');
    document.getElementById('product-content').classList.remove('hidden');
    document.getElementById('type-content').classList.add('hidden');
    updateNavActive('product');
}

function showTypeManagement() {
    document.getElementById('dashboard-content').classList.add('hidden');
    document.getElementById('user-content').classList.add('hidden');
    document.getElementById('order-content').classList.add('hidden');
    document.getElementById('product-content').classList.add('hidden');
    document.getElementById('type-content').classList.remove('hidden');
    updateNavActive('type');
}

function updateNavActive(activeItem) {
    const navItems = document.querySelectorAll('.nav-item');
    navItems.forEach(item => item.classList.remove('active'));
    
    if (activeItem === 'dashboard') {
        navItems[0].classList.add('active');
    } else if (activeItem === 'user') {
        navItems[1].classList.add('active');
    } else if (activeItem === 'order') {
        navItems[2].classList.add('active');
    } else if (activeItem === 'product') {
        navItems[3].classList.add('active');
    } else if (activeItem === 'type') {
        navItems[4].classList.add('active');
    }
}

        function loadUserList() {
            const container = document.getElementById('user-list-container');
            container.innerHTML = '<p>正在加载用户数据...</p>';

            fetch('http://localhost:8080/actionmall/user/finduserlist.do', {
                method: 'POST'
            })
            .then(response => response.json())
            .then(data => {
                if (data.status === 0) {
                    displayUserList(data.data);
                } else {
                    container.innerHTML = `<p style="color: red;">加载失败: ${data.msg}</p>`;
                }
            })
            .catch(error => {
                console.error('加载用户列表失败:', error);
                container.innerHTML = '<p style="color: red;">网络错误，请稍后重试</p>';
            });
        }

        function displayUserList(users) {
            const container = document.getElementById('user-list-container');
            
            if (!users || users.length === 0) {
                container.innerHTML = '<p>暂无用户数据</p>';
                return;
            }

            let html = `
                <table>
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>账号</th>
                            <th>姓名</th>
                            <th>邮箱</th>
                            <th>电话</th>
                            <th>性别</th>
                            <th>年龄</th>
                            <th>操作</th>
                        </tr>
                    </thead>
                    <tbody>
            `;

            users.forEach(user => {
                html += `
                    <tr>
                        <td>${user.id}</td>
                        <td>${user.account}</td>
                        <td>${user.name || '-'}</td>
                        <td>${user.email || '-'}</td>
                        <td>${user.phone || '-'}</td>
                        <td>${user.sex === true ? '男' : user.sex === false ? '女' : '-'}</td>
                        <td>${user.age || '-'}</td>
                        <td>
                            <button onclick="editUser(${user.id})" class="view-btn">编辑</button>
                            <button onclick="deleteUser(${user.id})" class="delete-btn">删除</button>
                        </td>
                    </tr>
                `;
            });

            html += `
                    </tbody>
                </table>
            `;

            container.innerHTML = html;
        }

        function editUser(userId) {
            // 先获取用户信息
            fetch('http://localhost:8080/actionmall/user/finduser.do', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded',
                },
                body: `id=${userId}`
            })
            .then(response => response.json())
            .then(data => {
                if (data.status === 0) {
                    showEditUserModal(data.data);
                } else {
                    alert('获取用户信息失败: ' + data.msg);
                }
            })
            .catch(error => {
                console.error('获取用户信息失败:', error);
                alert('网络错误，请稍后重试');
            });
        }

        function showEditUserModal(user) {
            const modal = document.createElement('div');
            modal.style.cssText = `
                position: fixed;
                top: 0;
                left: 0;
                width: 100%;
                height: 100%;
                background: rgba(0,0,0,0.5);
                display: flex;
                align-items: center;
                justify-content: center;
                z-index: 1000;
            `;

            const content = document.createElement('div');
            content.style.cssText = `
                background: white;
                padding: 30px;
                border-radius: 10px;
                max-width: 500px;
                width: 90%;
                max-height: 80vh;
                overflow-y: auto;
                position: relative;
            `;

            const closeBtn = document.createElement('button');
            closeBtn.textContent = '×';
            closeBtn.style.cssText = `
                position: absolute;
                top: 10px;
                right: 15px;
                background: none;
                border: none;
                font-size: 24px;
                cursor: pointer;
                color: #666;
            `;
            closeBtn.onclick = () => document.body.removeChild(modal);

            content.innerHTML = `
                <h3 style="margin-bottom: 20px;">编辑用户信息</h3>
                <form id="editUserForm">
                    <div style="margin-bottom: 15px;">
                        <label style="display: block; margin-bottom: 5px; font-weight: 600;">用户ID:</label>
                        <input type="text" id="editUserId" value="${user.id}" readonly style="width: 100%; padding: 8px; border: 1px solid #ddd; border-radius: 4px; background: #f5f5f5;">
                    </div>
                    <div style="margin-bottom: 15px;">
                        <label style="display: block; margin-bottom: 5px; font-weight: 600;">账号:</label>
                        <input type="text" id="editUserAccount" value="${user.account}" required style="width: 100%; padding: 8px; border: 1px solid #ddd; border-radius: 4px;">
                    </div>
                    <div style="margin-bottom: 15px;">
                        <label style="display: block; margin-bottom: 5px; font-weight: 600;">姓名:</label>
                        <input type="text" id="editUserName" value="${user.name || ''}" required style="width: 100%; padding: 8px; border: 1px solid #ddd; border-radius: 4px;">
                    </div>
                    <div style="margin-bottom: 15px;">
                        <label style="display: block; margin-bottom: 5px; font-weight: 600;">年龄:</label>
                        <input type="number" id="editUserAge" value="${user.age || ''}" required style="width: 100%; padding: 8px; border: 1px solid #ddd; border-radius: 4px;">
                    </div>
                    <div style="margin-bottom: 15px;">
                        <label style="display: block; margin-bottom: 5px; font-weight: 600;">电话:</label>
                        <input type="text" id="editUserPhone" value="${user.phone || ''}" required style="width: 100%; padding: 8px; border: 1px solid #ddd; border-radius: 4px;">
                    </div>
                    <div style="margin-bottom: 15px;">
                        <label style="display: block; margin-bottom: 5px; font-weight: 600;">邮箱:</label>
                        <input type="email" id="editUserEmail" value="${user.email || ''}" required style="width: 100%; padding: 8px; border: 1px solid #ddd; border-radius: 4px;">
                    </div>
                    <div style="margin-bottom: 20px;">
                        <label style="display: block; margin-bottom: 5px; font-weight: 600;">性别:</label>
                        <select id="editUserSex" required style="width: 100%; padding: 8px; border: 1px solid #ddd; border-radius: 4px;">
                            <option value="男" ${user.sex === '男' ? 'selected' : ''}>男</option>
                            <option value="女" ${user.sex === '女' ? 'selected' : ''}>女</option>
                        </select>
                    </div>
                    <div style="display: flex; gap: 10px;">
                        <button type="submit" style="flex: 1; padding: 10px; background: #667eea; color: white; border: none; border-radius: 5px; cursor: pointer;">保存</button>
                        <button type="button" onclick="document.body.removeChild(modal)" style="flex: 1; padding: 10px; background: #6c757d; color: white; border: none; border-radius: 5px; cursor: pointer;">取消</button>
                    </div>
                </form>
            `;

            content.appendChild(closeBtn);
            modal.appendChild(content);
            document.body.appendChild(modal);

            // 添加表单提交事件
            document.getElementById('editUserForm').addEventListener('submit', function(e) {
                e.preventDefault();
                
                const formData = {
                    id: document.getElementById('editUserId').value,
                    account: document.getElementById('editUserAccount').value,
                    name: document.getElementById('editUserName').value,
                    age: document.getElementById('editUserAge').value,
                    phone: document.getElementById('editUserPhone').value,
                    email: document.getElementById('editUserEmail').value,
                    sex: document.getElementById('editUserSex').value === '男' // 转换为布尔值
                };

                // 发送更新请求
                fetch('http://localhost:8080/actionmall/user/updateuser.do', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json', // 修改为JSON格式
                    },
                    body: JSON.stringify(formData)
                })
                .then(response => response.json())
                .then(data => {
                    if (data.status === 0) {
                        alert('用户信息更新成功');
                        document.body.removeChild(modal);
                        loadUserList(); // 重新加载用户列表
                    } else {
                        alert('更新失败: ' + data.msg);
                    }
                })
                .catch(error => {
                    console.error('更新用户信息失败:', error);
                    alert('网络错误，请稍后重试');
                });
            });
        }

        function deleteUser(userId) {
            if (!confirm('确定要删除这个用户吗？')) {
                return;
            }

            fetch('http://localhost:8080/actionmall/user/deleteusers.do', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded',
                },
                body: `id=${userId}`
            })
            .then(response => response.json())
            .then(data => {
                if (data.status === 0) {
                    alert('删除成功');
                    loadUserList(); // 重新加载用户列表
                } else {
                    alert('删除失败: ' + data.msg);
                }
            })
            .catch(error => {
                console.error('删除用户失败:', error);
                alert('网络错误，请稍后重试');
            });
        }

function loadOrderList() {
    const container = document.getElementById('order-list-container');
    container.innerHTML = '<p>正在加载订单数据...</p>';

    const pageNum = 1;  // 当前页
    const pageSize = 10;  // 每页显示50条数据

    // 发送请求时，传递pageNum和pageSize
    fetch('http://localhost:8080/actionmall/mgr/order/findorders.do', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
        },
        body: `pageNum=${pageNum}&pageSize=${pageSize}`  // 使用pageNum和pageSize
    })
        .then(response => response.json())
        .then(data => {
            if (data.status === 0) {
                displayOrderList(data.data);  // 假设displayOrderList是用来显示订单列表的函数
            } else {
                container.innerHTML = `<p style="color: red;">加载失败: ${data.msg}</p>`;
            }
        })
        .catch(error => {
            console.error('加载订单列表失败:', error);
            container.innerHTML = '<p style="color: red;">网络错误，请稍后重试</p>';
        });
}


function searchOrder() {
            const orderNo = document.getElementById('searchOrderNo').value;
            if (!orderNo) {
                alert('请输入订单号');
                return;
            }

            const container = document.getElementById('order-list-container');
            container.innerHTML = '<p>正在搜索订单...</p>';

            fetch(`http://localhost:8080/actionmall/mgr/order/findorders_nopages.do?orderNo=${orderNo}`,{
                method: 'GET'
            })
            .then(response => response.json())
            .then(data => {
                if (data.status === 0) {
                    displayOrderList(data);
                } else {
                    container.innerHTML = `<p style="color: red;">搜索失败: ${data.msg}</p>`;
                }
            })
            .catch(error => {
                console.error('搜索订单失败:', error);
                container.innerHTML = '<p style="color: red;">网络错误，请稍后重试</p>';
            });
        }

        function displayOrderList(orderData) {
            const container = document.getElementById('order-list-container');
            const orders = orderData.data || [];

            let html = `
                <table>
                    <thead>
                        <tr>
                            <th>订单号</th>
                            <th>金额</th>
                            <th>支付方式</th>
                            <th>状态</th>
                            <th>创建时间</th>
                            <th>操作</th>
                        </tr>
                    </thead>
                    <tbody>
            `;

            orders.forEach(order => {
                const statusText = order.statusDesc || getOrderStatusText(order.status);
                const typeText = order.typeDesc || '未知';
                const createTime = order.created ? new Date(order.created).toLocaleString() : '-';
                
                html += `
                    <tr>
                        <td>${order.orderNo}</td>
                        <td>￥${order.amount}</td>
                        <td>${typeText}</td>
                        <td>${statusText}</td>
                        <td>${createTime}</td>
                        <td>
                            <button onclick="viewOrderDetail(${order.orderNo})" class="view-btn">查看详情</button>
                        </td>
                    </tr>
                `;
            });

            html += `
                    </tbody>
                </table>
            `;

            container.innerHTML = html;
        }

function getOrderStatusText(status) {
    const statusMap = {
        0: '待付款',
        1: '已付款',
        2: '已发货',
        3: '已完成',
        4: '已取消'
    };
    return statusMap[status] || '未知状态';
}

function viewOrderDetail(orderNo) {
    fetch(`http://localhost:8080/actionmall/mgr/order/getdetail.do?orderNo=${orderNo}`)
    .then(response => response.json())
    .then(data => {
        if (data.status === 0) {
            showOrderDetailModal(data.data);
        } else {
            alert('获取订单详情失败: ' + data.msg);
        }
    })
    .catch(error => {
        console.error('获取订单详情失败:', error);
        alert('网络错误，请稍后重试');
    });
}

function showOrderDetailModal(order) {
    const modal = document.createElement('div');
    modal.style.cssText = `
        position: fixed;
        top: 0;
        left: 0;
        width: 100%;
        height: 100%;
        background: rgba(0,0,0,0.5);
        display: flex;
        align-items: center;
        justify-content: center;
        z-index: 1000;
    `;

    const content = document.createElement('div');
    content.style.cssText = `
        background: white;
        padding: 30px;
        border-radius: 10px;
        max-width: 600px;
        max-height: 80vh;
        overflow-y: auto;
        position: relative;
    `;

    const closeBtn = document.createElement('button');
    closeBtn.textContent = '×';
    closeBtn.style.cssText = `
        position: absolute;
        top: 10px;
        right: 15px;
        background: none;
        border: none;
        font-size: 24px;
        cursor: pointer;
        color: #666;
    `;
    closeBtn.onclick = () => document.body.removeChild(modal);

                const orderItems = order.orderItems ? order.orderItems.map(item => 
                `<div style="border-bottom: 1px solid #eee; padding: 10px 0;">
                    <div><strong>商品名称:</strong> ${item.goodsName || '未知商品'}</div>
                    <div><strong>商品ID:</strong> ${item.goodsId}</div>
                    <div><strong>数量:</strong> ${item.quantity}</div>
                    <div><strong>单价:</strong> ￥${item.price}</div>
                    <div><strong>小计:</strong> ￥￥${item.quantity * item.price}</div>
                </div>`
            ).join('') : '<p>暂无商品信息</p>';

            const address = order.address ? `
                <div style="margin-bottom: 15px;"><strong>收货地址:</strong></div>
                <div style="margin-bottom: 15px; padding: 10px; background: #f8f9fa; border-radius: 5px;">
                    <div>收货人: ${order.address.name}</div>
                    <div>电话: ${order.address.mobile || order.address.phone}</div>
                    <div>地址: ${order.address.province} ${order.address.city} ${order.address.district} ${order.address.addr}</div>
                    <div>邮编: ${order.address.zip}</div>
                </div>
            ` : '';

            content.innerHTML = `
                <h3 style="margin-bottom: 20px;">订单详情</h3>
                <div style="margin-bottom: 15px;"><strong>订单号:</strong> ${order.orderNo}</div>
                <div style="margin-bottom: 15px;"><strong>总金额:</strong> ￥${order.amount}</div>
                <div style="margin-bottom: 15px;"><strong>支付方式:</strong> ${order.typeDesc || '未知'}</div>
                <div style="margin-bottom: 15px;"><strong>订单状态:</strong> ${order.statusDesc || getOrderStatusText(order.status)}</div>
                <div style="margin-bottom: 15px;"><strong>运费:</strong> ￥${order.freight || 0}</div>
                <div style="margin-bottom: 15px;"><strong>创建时间:</strong> ${order.created ? new Date(order.created).toLocaleString() : '-'}</div>
                ${order.paymentTime ? `<div style="margin-bottom: 15px;"><strong>支付时间:</strong> ${order.paymentTime}</div>` : ''}
                ${order.deliveryTime ? `<div style="margin-bottom: 15px;"><strong>发货时间:</strong> ${order.deliveryTime}</div>` : ''}
                ${order.finishTime ? `<div style="margin-bottom: 15px;"><strong>完成时间:</strong> ${order.finishTime}</div>` : ''}
                ${order.closeTime ? `<div style="margin-bottom: 15px;"><strong>关闭时间:</strong> ${order.closeTime}</div>` : ''}
                ${address}
                <div style="margin-bottom: 15px;"><strong>商品信息:</strong></div>
                <div style="max-height: 200px; overflow-y: auto; border: 1px solid #eee; padding: 10px;">
                    ${orderItems}
                </div>
            `;

    content.appendChild(closeBtn);
    modal.appendChild(content);
    document.body.appendChild(modal);
}

function logout() {
    fetch('http://localhost:8080/actionmall/user/do_logout.do')
    .then(response => response.json())
    .then(data => {
        window.location.href = 'admin-login.html';
    })
    .catch(error => {
        console.error('退出登录失败:', error);
        window.location.href = 'admin-login.html';
    });
}
console.log('Offset:', offset);
console.log('PageSize:', pageSize);
console.log("Request body:", JSON.stringify(user));

// 商品管理主流程
function loadProductList(pageNum = 1, pageSize = 10) {
    const container = document.getElementById('product-list-container');
    container.innerHTML = '<p>正在加载商品数据...</p>';
    
    // 如果映射表为空，先加载商品类型列表
    if (productTypeMap.size === 0) {
        fetch('http://localhost:8080/actionmall/mgr/param/findptype.do')
        .then(response => response.json())
        .then(data => {
            if (data.status === 0) {
                // 建立映射表
                productTypeMap.clear();
                data.data.forEach(type => {
                    productTypeMap.set(type.id, type.name);
                });
                console.log('已建立商品类型映射表:', productTypeMap);
                // 映射表建立后，加载商品列表
                loadProductListData(pageNum, pageSize);
            } else {
                container.innerHTML = `<p style="color: red;">加载商品类型失败: ${data.msg}</p>`;
            }
        })
        .catch(error => {
            console.error('加载商品类型失败:', error);
            container.innerHTML = '<p style="color: red;">网络错误，请稍后重试</p>';
        });
    } else {
        // 映射表已存在，直接加载商品列表
        loadProductListData(pageNum, pageSize);
    }
}

function loadProductListData(pageNum = 1, pageSize = 10) {
    const container = document.getElementById('product-list-container');
    container.innerHTML = '<p>正在加载商品数据...</p>';
    
    fetch('http://localhost:8080/actionmall/mgr/product/searchproducts.do', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ pageNum, pageSize })
    })
    .then(response => response.json())
    .then(data => {
        if (data.status === 0) {
            displayProductList(data.data);
        } else {
            container.innerHTML = `<p style="color: red;">加载失败: ${data.msg}</p>`;
        }
    })
    .catch(error => {
        console.error('加载商品列表失败:', error);
        container.innerHTML = '<p style="color: red;">网络错误，请稍后重试</p>';
    });
}

function displayProductList(pageData) {
    const container = document.getElementById('product-list-container');
    const products = pageData.data || [];
    if (!products.length) {
        container.innerHTML = '<p>暂无商品数据</p>';
        return;
    }
    
    // 调试信息：检查映射表状态
    console.log('商品类型映射表:', productTypeMap);
    console.log('映射表大小:', productTypeMap.size);
    
    let html = `
        <table>
            <thead>
                <tr>
                    <th>ID</th>
                    <th>名称</th>
                    <th>价格</th>
                    <th>库存</th>
                    <th>状态</th>
                    <th>商品类型</th>
                    <th>是否热销</th>
                    <th>描述</th>
                    <th>操作</th>
                </tr>
            </thead>
            <tbody>
    `;
    products.forEach(product => {
        // 调试信息：检查每个商品的productId
        console.log(`商品 ${product.id} 的 productId:`, product.productId, '类型:', typeof product.productId);
        
        // 获取商品类型名称，确保类型转换正确
        let typeName = '-';
        if (product.productId !== null && product.productId !== undefined && product.productId !== '') {
            const productId = parseInt(product.productId);
            if (!isNaN(productId)) {
                typeName = productTypeMap.get(productId) || `未知类型(${productId})`;
            } else {
                typeName = `无效ID(${product.productId})`;
            }
        }
        // 是否热销
        const isHot = product.isHot === 1 ? '是' : '否';
        const hotBtnText = product.isHot === 1 ? '取消热销' : '设为热销';
        
        html += `
            <tr>
                <td>${product.id}</td>
                <td>${product.name || '-'}</td>
                <td>${product.price || '-'}</td>
                <td>${product.stock || '-'}</td>
                <td>${product.status === 2 ? '上架' : '下架'}</td>
                <td>${typeName}</td>
                <td>${isHot} <button onclick="toggleProductHot(${product.id},${product.isHot})" class="btn" style="padding:5px 10px;font-size:12px;">${hotBtnText}</button></td>
                <td>${product.detail ? product.detail.replace(/<[^>]+>/g, '').slice(0, 40) : '-'}</td>
                <td>
                    <button onclick="editProduct(${product.id})" class="view-btn">编辑</button>
                    <button onclick="deleteProduct(${product.id})" class="delete-btn">删除</button>
                    <button onclick="toggleProductStatus(${product.id},${product.status})" class="btn" style="padding:5px 10px;font-size:12px;">${product.status === 2 ? '下架' : '上架'}</button>
                </td>
            </tr>
        `;
    });
    html += '</tbody></table>';
    container.innerHTML = html;
}

function searchProduct() {
    const name = document.getElementById('searchProductName').value.trim();
    const container = document.getElementById('product-list-container');
    container.innerHTML = '<p>正在搜索商品...</p>';
    fetch('http://localhost:8080/actionmall/mgr/product/searchproducts.do', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ name })
    })
    .then(response => response.json())
    .then(data => {
        if (data.status === 0) {
            displayProductList(data.data);
        } else {
            container.innerHTML = `<p style="color: red;">搜索失败: ${data.msg}</p>`;
        }
    })
    .catch(error => {
        console.error('搜索商品失败:', error);
        container.innerHTML = '<p style="color: red;">网络错误，请稍后重试</p>';
    });
}

function deleteProduct(id) {
    if (!confirm('确定要删除该商品吗？')) return;
    fetch('http://localhost:8080/actionmall/mgr/product/delproduct.do', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ id })
    })
    .then(response => response.json())
    .then(data => {
        if (data.status === 0) {
            alert('删除成功');
            loadProductList();
        } else {
            alert('删除失败: ' + data.msg);
        }
    })
    .catch(error => {
        console.error('删除商品失败:', error);
        alert('网络错误，请稍后重试');
    });
}

function toggleProductStatus(id, status) {
    const newStatus = status === 1 ? 2 : 1;
    fetch('http://localhost:8080/actionmall/mgr/product/setstatus.do', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ id: id.toString(), status: newStatus.toString() })
    })
    .then(response => response.json())
    .then(data => {
        if (data.status === 0) {
            loadProductList();
        } else {
            alert('操作失败: ' + data.msg);
        }
    })
    .catch(error => {
        console.error('商品上下架失败:', error);
        alert('网络错误，请稍后重试');
    });
}

function toggleProductHot(id, isHot) {
    // 只切换热销状态，保持status不变
    const newHot = isHot === 1 ? 2 : 1;
    fetch('http://localhost:8080/actionmall/mgr/product/setstatus.do', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ productId: id, hot: newHot, status: null })
    })
    .then(response => response.json())
    .then(data => {
        if (data.status === 0) {
            loadProductList();
        } else {
            alert('操作失败: ' + data.msg);
        }
    })
    .catch(error => {
        console.error('商品热销状态切换失败:', error);
        alert('网络错误，请稍后重试');
    });
}

function showAddProductModal() {
    showProductModal();
}

function editProduct(id) {
    fetch('http://localhost:8080/actionmall/mgr/product/getdetail.do', {
        method: 'POST',
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
        body: `id=${id}`
    })
    .then(response => response.json())
    .then(data => {
        if (data) {
            showProductModal(data);
        } else {
            alert('获取商品信息失败');
        }
    })
    .catch(error => {
        console.error('获取商品信息失败:', error);
        alert('网络错误，请稍后重试');
    });
}

function showProductModal(product) {
    const modal = document.createElement('div');
    modal.style.cssText = `position: fixed; top: 0; left: 0; width: 100%; height: 100%; background: rgba(0,0,0,0.5); display: flex; align-items: center; justify-content: center; z-index: 1000;`;
    const content = document.createElement('div');
    content.style.cssText = `background: white; padding: 30px; border-radius: 10px; max-width: 500px; width: 90%; max-height: 80vh; overflow-y: auto; position: relative;`;
    const closeBtn = document.createElement('button');
    closeBtn.textContent = '×';
    closeBtn.style.cssText = `position: absolute; top: 10px; right: 15px; background: none; border: none; font-size: 24px; cursor: pointer; color: #666;`;
    closeBtn.onclick = () => document.body.removeChild(modal);
    
    // 生成顶级类型下拉选项 - 从所有类型中筛选出顶级类型
    let parentTypeOptions = '<option value="">请选择顶级类型</option>';
    
    // 从商品类型管理中获取所有类型数据来筛选顶级类型
    fetch('http://localhost:8080/actionmall/mgr/param/findptype.do')
    .then(response => response.json())
    .then(data => {
        if (data.status === 0) {
            // 筛选出顶级类型（parentId为0或null的类型）
            const topLevelTypes = data.data.filter(type => !type.parentId || type.parentId === 0);
            console.log('顶级类型列表:', topLevelTypes);
            
            topLevelTypes.forEach(type => {
                parentTypeOptions += `<option value="${type.id}">${type.name}</option>`;
            });
            
            // 更新顶级类型下拉框
            const parentSelect = document.getElementById('parentTypeSelect');
            if (parentSelect) {
                parentSelect.innerHTML = parentTypeOptions;
            }
        }
    })
    .catch(error => {
        console.error('获取顶级类型失败:', error);
    });
    
    // 生成子类型下拉选项（初始为空）
    let childTypeOptions = '<option value="">请先选择顶级类型</option>';
    
    content.innerHTML = `
        <h3 style="margin-bottom: 20px;">${product ? '编辑商品' : '添加商品'}</h3>
        <form id="productForm">
            <input type="hidden" id="productProductId" value="${product?.productId || ''}">
            <input type="hidden" id="productPartsId" value="${product?.partsId || ''}">
            <input type="hidden" id="productSpecParam" value="${product?.specParam || ''}">
            <input type="hidden" id="productSubImages" value="${product?.subImages || ''}">
            <input type="hidden" id="productProductTypeId" value="${product?.productTypeId || ''}">
            <div style="margin-bottom: 15px;">
                <label style="display: block; margin-bottom: 5px; font-weight: 600;">商品名称:</label>
                <input type="text" id="productName" value="${product?.name || ''}" required style="width: 100%; padding: 8px; border: 1px solid #ddd; border-radius: 4px;">
            </div>
            <div style="margin-bottom: 15px;">
                <label style="display: block; margin-bottom: 5px; font-weight: 600;">商品图片:</label>
                <input type="file" id="productImage" accept="image/*" style="width: 100%;">
                <input type="hidden" id="productIconUrl" value="${product?.iconUrl || ''}">
                <div id="productImagePreview" style="margin-top:8px;">
                    ${product?.iconUrl ? `<img src="${product.iconUrl}" style="width:60px;height:60px;object-fit:cover;">` : ''}
                </div>
            </div>
            <div style="margin-bottom: 15px;">
                <label style="display: block; margin-bottom: 5px; font-weight: 600;">顶级类型:</label>
                <select id="parentTypeSelect" required style="width: 100%; padding: 8px; border: 1px solid #ddd; border-radius: 4px;">
                    <option value="">正在加载顶级类型...</option>
                </select>
            </div>
            <div style="margin-bottom: 15px;">
                <label style="display: block; margin-bottom: 5px; font-weight: 600;">子类型:</label>
                <select id="childTypeSelect" required style="width: 100%; padding: 8px; border: 1px solid #ddd; border-radius: 4px;">
                    <option value="">请先选择顶级类型</option>
                </select>
            </div>
            <div style="margin-bottom: 15px;">
                <label style="display: block; margin-bottom: 5px; font-weight: 600;">价格:</label>
                <input type="number" id="productPrice" value="${product?.price || ''}" required style="width: 100%; padding: 8px; border: 1px solid #ddd; border-radius: 4px;">
            </div>
            <div style="margin-bottom: 15px;">
                <label style="display: block; margin-bottom: 5px; font-weight: 600;">库存:</label>
                <input type="number" id="productStock" value="${product?.stock || ''}" required style="width: 100%; padding: 8px; border: 1px solid #ddd; border-radius: 4px;">
            </div>
            <div style="margin-bottom: 15px;">
                <label style="display: block; margin-bottom: 5px; font-weight: 600;">商品描述:</label>
                <textarea id="productDetail" rows="4" style="width: 100%; padding: 8px; border: 1px solid #ddd; border-radius: 4px;">${product?.detail || ''}</textarea>
            </div>
            <div style="display: flex; gap: 10px;">
                <button type="submit" style="flex: 1; padding: 10px; background: #667eea; color: white; border: none; border-radius: 5px; cursor: pointer;">保存</button>
                <button type="button" onclick="document.body.removeChild(this.parentNode.parentNode.parentNode.parentNode)" style="flex: 1; padding: 10px; background: #6c757d; color: white; border: none; border-radius: 5px; cursor: pointer;">取消</button>
            </div>
        </form>
    `;
    content.appendChild(closeBtn);
    modal.appendChild(content);
    document.body.appendChild(modal);
    
    // 图片上传事件
    document.getElementById('productImage').addEventListener('change', function(e) {
        const file = e.target.files[0];
        if (!file) return;
        const formData = new FormData();
        formData.append('file', file);
        fetch('http://localhost:8080/actionmall/mgr/product/upload.do', {
            method: 'POST',
            body: formData
        })
        .then(response => response.json())
        .then(data => {
            if (data.status === 0 && data.data && data.data.url) {
                document.getElementById('productIconUrl').value = data.data.url;
                document.getElementById('productImagePreview').innerHTML = `<img src="${data.data.url}" style="width:60px;height:60px;object-fit:cover;">`;
            } else {
                alert('图片上传失败: ' + (data.msg || '未知错误'));
            }
        })
        .catch(() => {
            alert('图片上传失败');
        });
    });
    
    // 添加顶级类型选择事件
    document.getElementById('parentTypeSelect').addEventListener('change', function() {
        const parentId = this.value;
        const childSelect = document.getElementById('childTypeSelect');
        
        console.log('选择的顶级类型ID:', parentId, '名称:', productTypeMap.get(parseInt(parentId)));
        console.log('当前商品类型映射表:', productTypeMap);
        
        if (parentId) {
            // 从后端获取子类型数据
            childSelect.innerHTML = '<option value="">正在加载子类型...</option>';
            fetch('http://localhost:8080/actionmall/mgr/param/findchildren.do', {
                method: 'POST',
                headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
                body: `id=${parentId}`
            })
            .then(response => response.json())
            .then(data => {
                console.log('后端返回的子类型数据:', data);
                if (data.status === 0) {
                    let childOptions = '<option value="">请选择子类型</option>';
                    if (data.data && data.data.length > 0) {
                        data.data.forEach(type => {
                            console.log('子类型:', type);
                            childOptions += `<option value="${type.id}">${type.name}</option>`;
                        });
                    } else {
                        childOptions = '<option value="">该顶级类型下暂无子类型</option>';
                    }
                    childSelect.innerHTML = childOptions;
                } else {
                    console.error('获取子类型失败:', data.msg);
                    childSelect.innerHTML = `<option value="">加载失败: ${data.msg}</option>`;
                }
            })
            .catch(error => {
                console.error('获取子类型失败:', error);
                childSelect.innerHTML = '<option value="">加载子类型失败</option>';
            });
        } else {
            childSelect.innerHTML = '<option value="">请先选择顶级类型</option>';
        }
    });
    
    // 添加子类型选择事件
    document.getElementById('childTypeSelect').addEventListener('change', function() {
        const childId = this.value;
        console.log('选择的子类型ID:', childId, '名称:', productTypeMap.get(parseInt(childId)));
        console.log('当前商品类型映射表:', productTypeMap);
    });
    
    // 如果是编辑模式，设置当前选中的类型
    if (product && product.productId) {
        // 根据productId找到对应的父类型
        fetch('http://localhost:8080/actionmall/mgr/param/findptype.do')
        .then(response => response.json())
        .then(data => {
            if (data.status === 0) {
                // 找到当前商品的类型信息
                const currentType = data.data.find(type => type.id == product.productId);
                if (currentType) {
                    console.log('当前商品类型:', currentType);
                    
                    // 如果当前类型有父类型，先加载父类型
                    if (currentType.parentId && currentType.parentId !== 0) {
                        // 设置顶级类型
                        const parentSelect = document.getElementById('parentTypeSelect');
                        parentSelect.value = currentType.parentId;
                        
                        // 触发顶级类型选择事件，加载子类型
                        parentSelect.dispatchEvent(new Event('change'));
                        
                        // 等待子类型加载完成后设置选中值
                        setTimeout(() => {
                            const childSelect = document.getElementById('childTypeSelect');
                            childSelect.value = product.productId;
                        }, 500);
                    } else {
                        // 如果当前类型就是顶级类型，直接设置为顶级类型
                        const parentSelect = document.getElementById('parentTypeSelect');
                        parentSelect.value = product.productId;
                        
                        // 清空子类型选择
                        const childSelect = document.getElementById('childTypeSelect');
                        childSelect.innerHTML = '<option value="">该顶级类型下暂无子类型</option>';
                        childSelect.value = '';
                    }
                }
            }
        })
        .catch(error => {
            console.error('获取商品类型信息失败:', error);
        });
    }
    
    document.getElementById('productForm').addEventListener('submit', function(e) {
        e.preventDefault();
        const name = document.getElementById('productName').value;
        // productId为顶级类型ID，partsId为子类型ID
        const productIdRaw = document.getElementById('parentTypeSelect').value;
        const productId = productIdRaw === '' ? null : parseInt(productIdRaw);
        const partsIdRaw = document.getElementById('childTypeSelect').value;
        const partsId = partsIdRaw === '' ? null : parseInt(partsIdRaw);
        const price = document.getElementById('productPrice').value;
        const stock = document.getElementById('productStock').value;
        const detail = document.getElementById('productDetail').value;
        const iconUrl = document.getElementById('productIconUrl').value || '';
        // 隐藏字段
        const specParam = document.getElementById('productSpecParam').value || '';
        let subImages = document.getElementById('productSubImages').value || '';
        // 如果subImages为空，则用iconUrl
        if (!subImages && iconUrl) {
            subImages = iconUrl;
        }
        const productTypeIdRaw = document.getElementById('productProductTypeId').value || '';
        const productTypeId = productTypeIdRaw === '' ? null : parseInt(productTypeIdRaw);
        // 调试：打印最终要提交的类型ID和名称
        console.log('提交商品时的顶级类型ID:', productId, '名称:', productTypeMap.get(productId));
        console.log('提交商品时的子类型ID:', partsId, '名称:', productTypeMap.get(partsId));
        console.log('提交商品时的图片地址:', iconUrl);
        console.log('提交商品时的subImages:', subImages);
        saveProduct({
            id: product?.id,
            name,
            productId,   // 顶级类型ID
            price,
            stock,
            detail,
            partsId,     // 子类型ID
            iconUrl,     // 商品图片
            specParam,
            subImages,   // 商品图片也作为subImages
            productTypeId
        });
    });
}

function saveProduct(product) {
    fetch('http://localhost:8080/actionmall/mgr/product/saveproduct.do', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(product)
    })
    .then(response => response.json())
    .then(data => {
        if (data.status === 0) {
            alert('保存成功');
            document.body.removeChild(document.querySelector('div[style*="z-index: 1000"]'));
            loadProductList();
        } else {
            alert('保存失败: ' + data.msg);
        }
    })
    .catch(error => {
        alert('保存失败');
    });
}

// 产品类型管理主流程
function loadTypeList() {
    const container = document.getElementById('type-list-container');
    container.innerHTML = '<p>正在加载类型数据...</p>';
    fetch('http://localhost:8080/actionmall/mgr/param/findptype.do')
    .then(response => response.json())
    .then(data => {
        if (data.status === 0) {
            console.log('原始商品类型数据:', data.data);
            
            // 重新建立映射表
            productTypeMap.clear();
            parentChildTypeMap.clear();
            
            data.data.forEach(type => {
                console.log('处理类型:', type);
                // 建立ID到名称的映射
                productTypeMap.set(type.id, type.name);
                
                // 建立父子关系映射
                const parentId = type.parentId || 0;
                if (!parentChildTypeMap.has(parentId)) {
                    parentChildTypeMap.set(parentId, []);
                }
                parentChildTypeMap.get(parentId).push({
                    id: type.id,
                    name: type.name,
                    parentId: type.parentId
                });
            });
            
            console.log('重新生成的商品类型映射表:', productTypeMap);
            console.log('重新生成的父子类型映射表:', parentChildTypeMap);
            console.log('映射表大小:', productTypeMap.size);
            
            // 显示映射表详情
            console.log('=== 映射表详情 ===');
            productTypeMap.forEach((name, id) => {
                console.log(`ID ${id}: ${name}`);
            });
            
            console.log('=== 父子关系详情 ===');
            parentChildTypeMap.forEach((children, parentId) => {
                console.log(`父类型ID ${parentId}:`, children);
            });
            
            displayTypeList(data.data);
        } else {
            container.innerHTML = `<p style="color: red;">加载失败: ${data.msg}</p>`;
        }
    })
    .catch(error => {
        console.error('加载类型列表失败:', error);
        container.innerHTML = '<p style="color: red;">网络错误，请稍后重试</p>';
    });
}

function displayTypeList(types) {
    const container = document.getElementById('type-list-container');
    if (!types || !types.length) {
        container.innerHTML = '<p>暂无类型数据</p>';
        return;
    }
    let html = `
        <table>
            <thead>
                <tr>
                    <th>ID</th>
                    <th>类型名称</th>
                    <th>父类型</th>
                    <th>操作</th>
                </tr>
            </thead>
            <tbody>
    `;
    types.forEach(type => {
        // 获取父类型名称
        const parentName = type.parentId && type.parentId !== 0 ? 
            (productTypeMap.get(type.parentId) || `未知类型(${type.parentId})`) : 
            '顶级类型';
        
        html += `
            <tr>
                <td>${type.id}</td>
                <td>${type.name || '-'}</td>
                <td>${parentName}</td>
                <td>
                    <button onclick="editType(${type.id}, '${type.name || ''}', ${type.parentId || 0})" class="view-btn">编辑</button>
                    <button onclick="deleteType(${type.id})" class="delete-btn">删除</button>
                </td>
            </tr>
        `;
    });
    html += '</tbody></table>';
    container.innerHTML = html;
}

function deleteType(id) {
    if (!confirm('确定要删除该类型吗？')) return;
    fetch('http://localhost:8080/actionmall/mgr/param/delparam.do', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ id })
    })
    .then(response => response.json())
    .then(data => {
        if (data.status === 0) {
            alert('删除成功');
            loadTypeList();
        } else {
            alert('删除失败: ' + data.msg);
        }
    })
    .catch(error => {
        console.error('删除类型失败:', error);
        alert('网络错误，请稍后重试');
    });
}

function showAddTypeModal() {
    showTypeModal();
}

function editType(id, name, parentId) {
    showTypeModal({ id, name, parentId });
}

function showTypeModal(type) {
    const modal = document.createElement('div');
    modal.style.cssText = `position: fixed; top: 0; left: 0; width: 100%; height: 100%; background: rgba(0,0,0,0.5); display: flex; align-items: center; justify-content: center; z-index: 1000;`;
    const content = document.createElement('div');
    content.style.cssText = `background: white; padding: 30px; border-radius: 10px; max-width: 400px; width: 90%; max-height: 80vh; overflow-y: auto; position: relative;`;
    const closeBtn = document.createElement('button');
    closeBtn.textContent = '×';
    closeBtn.style.cssText = `position: absolute; top: 10px; right: 15px; background: none; border: none; font-size: 24px; cursor: pointer; color: #666;`;
    closeBtn.onclick = () => document.body.removeChild(modal);
    
    // 设置默认值
    const currentTime = new Date().toISOString();
    const defaultStatus = '1'; // 默认启用状态
    const defaultRemark = '';
    
    // 生成父类型下拉选项
    let parentOptions = '<option value="0">顶级类型</option>';
    productTypeMap.forEach((name, id) => {
        const selected = type && type.parentId == id ? 'selected' : '';
        parentOptions += `<option value="${id}" ${selected}>${name}</option>`;
    });
    
    content.innerHTML = `
        <h3 style="margin-bottom: 20px;">${type ? '编辑类型' : '添加类型'}</h3>
        <form id="typeForm">
            <input type="hidden" id="typeId" value="${type?.id || ''}">
            <input type="hidden" id="typeCreateTime" value="${type?.createTime || currentTime}">
            <input type="hidden" id="typeUpdateTime" value="${currentTime}">
            <input type="hidden" id="typeRemark" value="${type?.remark || defaultRemark}">
            <input type="hidden" id="typeStatus" value="${type?.status || defaultStatus}">
            <input type="hidden" id="typeChildren" value="${type?.children ? JSON.stringify(type.children) : '[]'}">
            <input type="hidden" id="typeProducts" value="${type?.products ? JSON.stringify(type.products) : '[]'}">
            <input type="hidden" id="typeParentId" value="${type?.parentId || ''}">
            <div style="margin-bottom: 15px;">
                <label style="display: block; margin-bottom: 5px; font-weight: 600;">类型名称:</label>
                <input type="text" id="typeName" value="${type?.name || ''}" required style="width: 100%; padding: 8px; border: 1px solid #ddd; border-radius: 4px;">
            </div>
            ${!type ? `
            <div style="margin-bottom: 15px;">
                <label style="display: block; margin-bottom: 5px; font-weight: 600;">父类型:</label>
                <select id="typeParentIdInput" style="width: 100%; padding: 8px; border: 1px solid #ddd; border-radius: 4px;">
                    ${parentOptions}
                </select>
            </div>
            ` : ''}
            <div style="display: flex; gap: 10px;">
                <button type="submit" style="flex: 1; padding: 10px; background: #667eea; color: white; border: none; border-radius: 5px; cursor: pointer;">保存</button>
                <button type="button" onclick="document.body.removeChild(this.parentNode.parentNode.parentNode.parentNode)" style="flex: 1; padding: 10px; background: #6c757d; color: white; border: none; border-radius: 5px; cursor: pointer;">取消</button>
            </div>
        </form>
    `;
    content.appendChild(closeBtn);
    modal.appendChild(content);
    document.body.appendChild(modal);
    document.getElementById('typeForm').addEventListener('submit', function(e) {
        e.preventDefault();
        const id = document.getElementById('typeId').value || '';
        const name = document.getElementById('typeName').value;
        const parentId = document.getElementById('typeParentIdInput') ? document.getElementById('typeParentIdInput').value : document.getElementById('typeParentId').value || '';
        const createTime = document.getElementById('typeCreateTime').value || currentTime;
        const updateTime = document.getElementById('typeUpdateTime').value || currentTime;
        const remark = document.getElementById('typeRemark').value || defaultRemark;
        const status = document.getElementById('typeStatus').value || defaultStatus;
        const children = document.getElementById('typeChildren').value || '[]';
        const products = document.getElementById('typeProducts').value || '[]';
        saveType({
            id,
            name,
            parentId,
            createTime,
            updateTime,
            remark,
            status,
            children,
            products
        });
    });
}

function saveType(type) {
    // 根据是否有id判断是新增还是编辑
    if (type.id) {
        // 编辑：只传递id和name
        fetch('http://localhost:8080/actionmall/mgr/param/updateparam.do', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({
                id: type.id,
                name: type.name
            })
        })
        .then(response => response.json())
        .then(data => {
            if (data.status === 0) {
                alert('更新成功');
                document.body.removeChild(document.querySelector('div[style*="z-index: 1000"]'));
                loadTypeList();
            } else {
                alert('更新失败: ' + data.msg);
            }
        })
        .catch(error => {
            alert('更新失败');
        });
    } else {
        // 新增：传递完整字段
        fetch('http://localhost:8080/actionmall/mgr/param/saveparam.do', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({
                name: type.name,
                parentId: type.parentId || '0',
                sortOrder: '1' // 默认排序
            })
        })
        .then(response => response.json())
        .then(data => {
            if (data.status === 0) {
                alert('新增成功');
                document.body.removeChild(document.querySelector('div[style*="z-index: 1000"]'));
                loadTypeList();
            } else {
                alert('新增失败: ' + data.msg);
            }
        })
        .catch(error => {
            alert('新增失败');
        });
    }

}