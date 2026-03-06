import request from '@/utils/request'

// 前台：上架演出列表（按推荐排序）
export function listPerformance(query) {
  return request({
    url: '/front/ticket/performance/list',
    method: 'get',
    params: query
  })
}

// 前台：演出详情及场次列表
export function getPerformanceDetail(performanceId) {
  return request({
    url: '/front/ticket/performance/' + performanceId,
    method: 'get'
  })
}

// 前台：场次座位列表
export function getSessionSeats(sessionId) {
  return request({
    url: '/front/ticket/session/' + sessionId + '/seats',
    method: 'get'
  })
}

// 前台：选座下单
export function placeOrder(sessionId, seatIds) {
  const params = new URLSearchParams()
  params.append('sessionId', sessionId)
  seatIds.forEach(id => params.append('seatIds', id))
  return request({
    url: '/front/ticket/order/place',
    method: 'post',
    params
  })
}

// 前台：我的订单列表
export function listMyOrders(query) {
  return request({
    url: '/front/ticket/order/list',
    method: 'get',
    params: query
  })
}

// 前台：我的收藏列表（含演出详情）
export function listMyFavorites(query) {
  return request({
    url: '/front/ticket/favorite/list',
    method: 'get',
    params: query
  })
}

// 前台：检查是否已收藏
export function checkFavorite(performanceId) {
  return request({
    url: '/front/ticket/favorite/check',
    method: 'get',
    params: { performanceId }
  })
}

// 前台：获取当前用户已收藏的演出ID列表
export function getMyFavoriteIds() {
  return request({
    url: '/front/ticket/favorite/ids',
    method: 'get'
  })
}

// 前台：收藏剧目
export function addFavorite(performanceId) {
  return request({
    url: '/front/ticket/favorite/add',
    method: 'post',
    params: { performanceId }
  })
}

// 前台：取消收藏
export function removeFavorite(performanceId) {
  return request({
    url: '/front/ticket/favorite/remove',
    method: 'post',
    params: { performanceId }
  })
}

// 发起支付宝支付（返回 HTML 表单，responseType: 'text'）
export function alipayPay(orderNo) {
  return request({
    url: '/order/alipay/pay',
    method: 'get',
    params: { orderNo },
    responseType: 'text'
  })
}
