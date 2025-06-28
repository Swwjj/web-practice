/**
 * [Mock] 首页楼层商品数据接口
 */
export function getFloorProducts() {
  return Promise.resolve({
    status: 0,
    data: {
      oneFloor: [
        {
          id: 6,
          name: "锂基脂 00#",
          productId: 10023,
          partsId: 10044,
          iconUrl: "/upload/10a42221-06a8-4001-8cc6-b2deb3b9e964.png",
          subImages: "/upload/10a42221-06a8-4001-8cc6-b2deb3b9e964.png,/upload/6308150b-1afd-41e7-acfa-45627015aa37.png",
          detail: "商品详情",
          specParam: "规格参数",
          price: 215.89,
          stock: 82,
          status: 2,
          hot: 1,
          created: 1519095352000,
          updated: 1554773510000
        },
        {
          id: 8,
          name: "润滑油",
          productId: 10023,
          partsId: 10038,
          iconUrl: "",
          subImages: "",
          detail: "优质润滑油",
          specParam: null,
          price: 79,
          stock: 74,
          status: 2,
          hot: 2,
          created: 1551258352000,
          updated: 1554280269000
        }
      ],
      twoFloor: [
        {
          id: 19,
          name: "机械配件",
          productId: 10024,
          partsId: 10051,
          iconUrl: "",
          subImages: "/upload/f54d9f3e-b573-4f16-894b-ee11ba39252a.png",
          detail: "机械配件详情",
          specParam: null,
          price: 299,
          stock: 11,
          status: 2,
          hot: 2,
          created: 1552292883000,
          updated: 1552974396000
        }
      ],
      threeFloor: [
        {
          id: 29,
          name: "压路机",
          productId: 10025,
          partsId: 10068,
          iconUrl: "",
          subImages: "/upload/95686505-35b3-428d-989d-5203181eb647.png",
          detail: "压路机详情",
          specParam: null,
          price: 15999,
          stock: 100,
          status: 2,
          hot: 2,
          created: 1552297179000,
          updated: 1554280269000
        }
      ],
      fourFloor: []
    }
  });
}



/**
 * [Mock] 热销商品接口
 */
export function getHotProducts(num = 5) {
  return Promise.resolve({
    status: 0,
    data: [
      {
        id: 11,
        name: "润滑油",
        productId: 10023,
        partsId: 10042,
        iconUrl: "",
        subImages: "",
        detail: "",
        specParam: null,
        price: 79,
        stock: 100,
        status: 2,
        hot: 1,
        created: 1552265825000,
        updated: 1552292521000
      },
      {
        id: 9,
        name: "润滑油",
        productId: 10023,
        partsId: 10042,
        iconUrl: "",
        subImages: "",
        detail: "好！",
        specParam: null,
        price: 79,
        stock: 92,
        status: 2,
        hot: 1,
        created: 1552265754000,
        updated: 1552614184000
      },
      {
        id: 10,
        name: "润滑油",
        productId: 10023,
        partsId: 10042,
        iconUrl: "",
        subImages: "",
        detail: "好！",
        specParam: null,
        price: 79,
        stock: 98,
        status: 2,
        hot: 1,
        created: 1552265800000,
        updated: 1553069114000
      },
      {
        id: 7,
        name: "锂基脂 00#",
        productId: 10023,
        partsId: 10044,
        iconUrl: "",
        subImages: "",
        detail: "商品详情",
        specParam: "规格参数",
        price: 215.89,
        stock: 86,
        status: 2,
        hot: 1,
        created: 1519095355000,
        updated: 1553069114000
      },
      {
        id: 6,
        name: "锂基脂 00#",
        productId: 10023,
        partsId: 10044,
        iconUrl: "/upload/10a42221-06a8-4001-8cc6-b2deb3b9e964.png",
        subImages: "/upload/10a42221-06a8-4001-8cc6-b2deb3b9e964.png,/upload/6308150b-1afd-41e7-acfa-45627015aa37.png",
        detail: "商品详情",
        specParam: "规格参数",
        price: 215.89,
        stock: 82,
        status: 2,
        hot: 1,
        created: 1519095352000,
        updated: 1554773510000
      }
    ].slice(0, num) // 根据传入的 num 参数返回对应数量的商品
  });
}



/**
 * [Mock] 商品详情接口
 */
export function getProductDetail(productId) {
  return Promise.resolve({
    status: 0,
    data: {
      id: 6,
      name: "锂基脂 00#",
      productId: 10023,
      partsId: 10044,
      iconUrl: "/upload/10a42221-06a8-4001-8cc6-b2deb3b9e964.png",
      subImages: "/upload/10a42221-06a8-4001-8cc6-b2deb3b9e964.png,/upload/6308150b-1afd-41e7-acfa-45627015aa37.png",
      detail: "商品详情",
      specParam: "规格参数",
      price: 215.89,
      stock: 82,
      status: 2,
      hot: 1,
      created: 1519095352000,
      updated: 1554773510000
    }
  });
}



/**
 * [Mock] 商品分页列表接口
 */
export function getProductList(params = {}) {
  const {
    productTypeId = '0',
    partsId = '0', 
    pageNum = '1',
    pageSize = '10',
    name = ''
  } = params;

  // Mock 商品数据
  const mockProducts = [
    {
      id: 6,
      name: "锂基脂 00#",
      price: 215.89,
      status: 2,
      statusDesc: "在售",
      productCategory: "混凝土机械",
      partsCategory: "锂基油",
      iconUrl: "/upload/10a42221-06a8-4001-8cc6-b2deb3b9e964.png",
      hot: 1,
      hotStatus: "热销"
    },
    {
      id: 7,
      name: "锂基脂 00#",
      price: 215.89,
      status: 2,
      statusDesc: "在售",
      productCategory: "混凝土机械",
      partsCategory: "锂基油",
      iconUrl: "",
      hot: 1,
      hotStatus: "热销"
    },
    {
      id: 8,
      name: "润滑油",
      price: 79.00,
      status: 2,
      statusDesc: "在售",
      productCategory: "压路机",
      partsCategory: "润滑油",
      iconUrl: "",
      hot: 0,
      hotStatus: ""
    },
    {
      id: 9,
      name: "机械配件A",
      price: 156.50,
      status: 2,
      statusDesc: "在售",
      productCategory: "挖掘机",
      partsCategory: "配件",
      iconUrl: "",
      hot: 1,
      hotStatus: "热销"
    },
    {
      id: 10,
      name: "液压油",
      price: 89.99,
      status: 2,
      statusDesc: "在售",
      productCategory: "装载机",
      partsCategory: "液压油",
      iconUrl: "",
      hot: 0,
      hotStatus: ""
    }
  ];

  // 筛选逻辑
  let filteredProducts = mockProducts.filter(product => {
    // 名称筛选
    if (name && !product.name.toLowerCase().includes(name.toLowerCase())) {
      return false;
    }
    
    // 产品类型筛选
    if (productTypeId !== '0') {
      const productTypeMap = {
        '1': '混凝土机械',
        '2': '压路机', 
        '3': '挖掘机',
        '4': '装载机'
      };
      if (product.productCategory !== productTypeMap[productTypeId]) {
        return false;
      }
    }
    
    // 配件类型筛选
    if (partsId !== '0') {
      const partsTypeMap = {
        '1': '锂基油',
        '2': '润滑油',
        '3': '液压油', 
        '4': '配件'
      };
      if (product.partsCategory !== partsTypeMap[partsId]) {
        return false;
      }
    }
    
    return true;
  });

  // 分页计算
  const totalRecord = filteredProducts.length;
  const pageSizeNum = parseInt(pageSize);
  const pageNumNum = parseInt(pageNum);
  const totalPage = Math.ceil(totalRecord / pageSizeNum);
  const startIndex = (pageNumNum - 1) * pageSizeNum;
  const endIndex = startIndex + pageSizeNum;
  const pageData = filteredProducts.slice(startIndex, endIndex);

  return Promise.resolve({
    status: 0,
    data: {
      pageNum: pageNumNum,
      pageSize: pageSizeNum,
      totalRecord,
      totalPage,
      startIndex,
      data: pageData,
      prePage: pageNumNum > 1 ? pageNumNum - 1 : 1,
      nextPage: pageNumNum < totalPage ? pageNumNum + 1 : totalPage
    }
  });
}





// import request from './request';

// /**
// * [真实请求] 首页楼层商品数据接口
// */
// export function getFloorProducts() {
//   return request({
//     url: '/product/findfloors.do',
//     method: 'post',
//     data: {} // 如果接口不需要参数，传空对象
//   });
// } 

// /**
// * [真实请求] 热销商品接口
// */
// export function getHotProducts(num = 5) {
//   return request({
//     url: '/product/findhotproducts.do',
//     method: 'post',
//     data: { num }
//   });
// } 

// /**
// * [真实请求] 商品详情接口
// */
// export function getProductDetail(productId) {
//   return request({
//     url: '/product/getdetail.do',
//     method: 'post',
//     data: { productId }
//   });
// } 

// /**
// * [真实请求] 商品分页列表接口
// */
// export function getProductList(params = {}) {
//   return request({
//     url: '/product/findproducts.do',
//     method: 'post',
//     data: {
//       productTypeId: params.productTypeId || '0',
//       partsId: params.partsId || '0',
//       pageNum: params.pageNum || '1',
//       pageSize: params.pageSize || '10',
//       name: params.name || ''
//     }
//   });
// } 