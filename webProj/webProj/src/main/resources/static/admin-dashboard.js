// 页面加载时检查登录状态
window.onload = function() {
    checkLoginStatus();
    loadDashboardStats();
};

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
    
    // 更新导航状态
    updateNavActive('dashboard');
}

function showUserManagement() {
    document.getElementById('dashboard-content').classList.add('hidden');
    document.getElementById('user-content').classList.remove('hidden');
    document.getElementById('order-content').classList.add('hidden');
    
    // 更新导航状态
    updateNavActive('user');
}

function showOrderManagement() {
    document.getElementById('dashboard-content').classList.add('hidden');
    document.getElementById('user-content').classList.add('hidden');
    document.getElementById('order-content').classList.remove('hidden');
    
    // 更新导航状态
    updateNavActive('order');
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
                    <div><strong>单价:</strong> ￥${item.curPrice}</div>
                    <div><strong>小计:</strong> ￥${item.totalPrice}</div>
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