import request from '@/utils/request'

// 查询积分兑换商品列表
export function listPointsItem(query) {
  return request({
    url: '/ticket/pointsItem/list',
    method: 'get',
    params: query
  })
}

// 查询积分兑换商品详细
export function getPointsItem(itemId) {
  return request({
    url: '/ticket/pointsItem/' + itemId,
    method: 'get'
  })
}

// 新增积分兑换商品
export function addPointsItem(data) {
  return request({
    url: '/ticket/pointsItem',
    method: 'post',
    data: data
  })
}

// 修改积分兑换商品
export function updatePointsItem(data) {
  return request({
    url: '/ticket/pointsItem/edit',
    method: 'post',
    data: data
  })
}

// 删除积分兑换商品
export function delPointsItem(itemIds) {
  return request({
    url: '/ticket/pointsItem/remove',
    method: 'post',
    params: { itemIds }
  })
}
