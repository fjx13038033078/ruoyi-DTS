import request from '@/utils/request'

/**
 * 获取上架的积分兑换商品列表
 * @param {Object} query 查询参数（支持分页）
 */
export function listPointsItems(query) {
  return request({
    url: '/front/ticket/points/item/list',
    method: 'get',
    params: query
  })
}

/**
 * 获取当前用户的积分兑换记录
 * @param {Object} query 查询参数（支持分页）
 */
export function listPointsRecords(query) {
  return request({
    url: '/front/ticket/points/record/list',
    method: 'get',
    params: query
  })
}

/**
 * 发起积分兑换
 * @param {Number} itemId 商品ID
 */
export function exchangePointsItem(itemId) {
  return request({
    url: '/front/ticket/points/exchange',
    method: 'post',
    params: { itemId }
  })
}
