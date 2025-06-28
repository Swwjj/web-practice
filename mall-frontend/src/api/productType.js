// 产品类型模块 API
// mock 版本默认导出，真实请求用注释保留，切换时只需注释/取消注释

// mock 数据 - 递归获取产品类型数据
const mockProductTypes = [
  {
    "id": 10023,
    "parent_id": 0,
    "name": "混凝土机械",
    "status": true,
    "sort_order": 1,
    "level": 0,
    "created": 1518421458000,
    "updated": 1518421458000,
    "children": [
      {
        "id": 10064,
        "parent_id": 10023,
        "name": "电器元件",
        "status": true,
        "sort_order": 0,
        "level": 1,
        "created": 1551679846000,
        "updated": 1551679846000,
        "children": []
      },
      {
        "id": 10032,
        "parent_id": 10023,
        "name": "泵送搅拌系统",
        "status": true,
        "sort_order": 1,
        "level": 1,
        "created": 1518421618000,
        "updated": 1518421618000,
        "children": [
          {
            "id": 10038,
            "parent_id": 10032,
            "name": "管阀类",
            "status": true,
            "sort_order": 1,
            "level": 2,
            "created": 1518421706000,
            "updated": 1518421706000,
            "children": []
          },
          {
            "id": 10039,
            "parent_id": 10032,
            "name": "易损类",
            "status": true,
            "sort_order": 2,
            "level": 2,
            "created": 1518421721000,
            "updated": 1518421721000,
            "children": []
          }
        ]
      },
      {
        "id": 10033,
        "parent_id": 10023,
        "name": "油品",
        "status": true,
        "sort_order": 2,
        "level": 1,
        "created": 1518421628000,
        "updated": 1518421628000,
        "children": [
          {
            "id": 10040,
            "parent_id": 10033,
            "name": "防冻液",
            "status": true,
            "sort_order": 1,
            "level": 2,
            "created": 1518421747000,
            "updated": 1518421747000,
            "children": []
          },
          {
            "id": 10041,
            "parent_id": 10033,
            "name": "齿轮油",
            "status": true,
            "sort_order": 2,
            "level": 2,
            "created": 1518421757000,
            "updated": 1518421757000,
            "children": []
          },
          {
            "id": 10042,
            "parent_id": 10033,
            "name": "润滑油",
            "status": true,
            "sort_order": 3,
            "level": 2,
            "created": 1518421766000,
            "updated": 1518421766000,
            "children": []
          },
          {
            "id": 10043,
            "parent_id": 10033,
            "name": "液压油",
            "status": true,
            "sort_order": 4,
            "level": 2,
            "created": 1518421775000,
            "updated": 1518421775000,
            "children": []
          },
          {
            "id": 10044,
            "parent_id": 10033,
            "name": "锂基油",
            "status": true,
            "sort_order": 5,
            "level": 2,
            "created": 1518421798000,
            "updated": 1518421798000,
            "children": []
          }
        ]
      },
      {
        "id": 10034,
        "parent_id": 10023,
        "name": "电器元件",
        "status": true,
        "sort_order": 3,
        "level": 1,
        "created": 1518421637000,
        "updated": 1518421637000,
        "children": [
          {
            "id": 10045,
            "parent_id": 10034,
            "name": "接触器",
            "status": true,
            "sort_order": 1,
            "level": 2,
            "created": 1518421835000,
            "updated": 1518421835000,
            "children": []
          },
          {
            "id": 10046,
            "parent_id": 10034,
            "name": "开关",
            "status": true,
            "sort_order": 2,
            "level": 2,
            "created": 1518421843000,
            "updated": 1518421843000,
            "children": []
          },
          {
            "id": 10047,
            "parent_id": 10034,
            "name": "继电器",
            "status": true,
            "sort_order": 3,
            "level": 2,
            "created": 1518421850000,
            "updated": 1518421850000,
            "children": []
          },
          {
            "id": 10048,
            "parent_id": 10034,
            "name": "遥控器",
            "status": true,
            "sort_order": 4,
            "level": 2,
            "created": 1518421856000,
            "updated": 1518421856000,
            "children": []
          },
          {
            "id": 10049,
            "parent_id": 10034,
            "name": "断路器",
            "status": true,
            "sort_order": 5,
            "level": 2,
            "created": 1518421877000,
            "updated": 1518421877000,
            "children": []
          },
          {
            "id": 10050,
            "parent_id": 10034,
            "name": "控制器",
            "status": true,
            "sort_order": 6,
            "level": 2,
            "created": 1518421886000,
            "updated": 1518421886000,
            "children": []
          }
        ]
      },
      {
        "id": 10035,
        "parent_id": 10023,
        "name": "地盘配件",
        "status": true,
        "sort_order": 4,
        "level": 1,
        "created": 1518421648000,
        "updated": 1518421648000,
        "children": []
      },
      {
        "id": 10036,
        "parent_id": 10023,
        "name": "发动机件",
        "status": true,
        "sort_order": 5,
        "level": 1,
        "created": 1518421662000,
        "updated": 1518421662000,
        "children": []
      },
      {
        "id": 10037,
        "parent_id": 10023,
        "name": "轮胎",
        "status": true,
        "sort_order": 6,
        "level": 1,
        "created": 1518421670000,
        "updated": 1518421670000,
        "children": []
      }
    ]
  },
  {
    "id": 10024,
    "parent_id": 0,
    "name": "建筑起重机械",
    "status": true,
    "sort_order": 2,
    "level": 0,
    "created": 1518421473000,
    "updated": 1518421473000,
    "children": [
      {
        "id": 10051,
        "parent_id": 10024,
        "name": "轮胎起重机",
        "status": true,
        "sort_order": 1,
        "level": 1,
        "created": 1518426384000,
        "updated": 1518426384000,
        "children": []
      }
    ]
  },
  {
    "id": 10025,
    "parent_id": 0,
    "name": "路面机械",
    "status": true,
    "sort_order": 3,
    "level": 0,
    "created": 1518421483000,
    "updated": 1518421483000,
    "children": [
      {
        "id": 10068,
        "parent_id": 10025,
        "name": "压路机",
        "status": true,
        "sort_order": 0,
        "level": 1,
        "created": 1552297010000,
        "updated": 1552297010000,
        "children": []
      }
    ]
  },
  {
    "id": 10026,
    "parent_id": 0,
    "name": "土方机械",
    "status": true,
    "sort_order": 4,
    "level": 0,
    "created": 1518421493000,
    "updated": 1518421493000,
    "children": [
      {
        "id": 10069,
        "parent_id": 10026,
        "name": "推土机",
        "status": true,
        "sort_order": 0,
        "level": 1,
        "created": 1552297023000,
        "updated": 1552297023000,
        "children": []
      }
    ]
  },
  {
    "id": 10027,
    "parent_id": 0,
    "name": "环卫机械",
    "status": true,
    "sort_order": 5,
    "level": 0,
    "created": 1518421502000,
    "updated": 1518421502000,
    "children": []
  },
  {
    "id": 10028,
    "parent_id": 0,
    "name": "工业车辆",
    "status": true,
    "sort_order": 6,
    "level": 0,
    "created": 1518421513000,
    "updated": 1518421513000,
    "children": [
      {
        "id": 10052,
        "parent_id": 10028,
        "name": "内燃平衡重叉车",
        "status": true,
        "sort_order": 1,
        "level": 1,
        "created": 1518426459000,
        "updated": 1518426459000,
        "children": [
          {
            "id": 10053,
            "parent_id": 10052,
            "name": "Z系列",
            "status": true,
            "sort_order": 1,
            "level": 2,
            "created": 1518426485000,
            "updated": 1518426485000,
            "children": []
          },
          {
            "id": 10054,
            "parent_id": 10052,
            "name": "E系列",
            "status": true,
            "sort_order": 2,
            "level": 2,
            "created": 1518426495000,
            "updated": 1518426495000,
            "children": []
          }
        ]
      }
    ]
  },
  {
    "id": 10029,
    "parent_id": 0,
    "name": "模型专区",
    "status": true,
    "sort_order": 7,
    "level": 0,
    "created": 1518421522000,
    "updated": 1518421522000,
    "children": []
  },
  {
    "id": 10030,
    "parent_id": 0,
    "name": "特辑专区",
    "status": true,
    "sort_order": 8,
    "level": 0,
    "created": 1518421536000,
    "updated": 1518421536000,
    "children": []
  },
  {
    "id": 10031,
    "parent_id": 0,
    "name": "运费-01",
    "status": true,
    "sort_order": 9,
    "level": 0,
    "created": 1518421543000,
    "updated": 1518421932000,
    "children": [
      {
        "id": 10055,
        "parent_id": 10031,
        "name": "111",
        "status": true,
        "sort_order": 0,
        "level": 1,
        "created": 1551665417000,
        "updated": 1551665417000,
        "children": []
      }
    ]
  }
];

/**
 * 递归获取产品类型数据
 * 获取产品参数的树形结构数据
 */
export function getProductTypes() {
  // mock 版本 - 直接返回模拟数据
  return Promise.resolve({
    status: 0,
    data: mockProductTypes
  });
}

// 真实请求版本（注释保留，切换时只需注释/取消注释）
// import request from './request';
// 
// /**
//  * 递归获取产品类型数据
//  * 获取产品参数的树形结构数据
//  */
// export function getProductTypes() {
//   return request({
//     url: '/actionmall/param/findallparams.do',
//     method: 'get'
//   });
// } 