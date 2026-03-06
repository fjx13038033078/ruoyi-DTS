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
