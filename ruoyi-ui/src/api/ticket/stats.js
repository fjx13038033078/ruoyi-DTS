import request from '@/utils/request'

// 售票统计（日/周/月）
export function getSalesStats(period) {
  return request({
    url: '/ticket/stats/sales',
    method: 'get',
    params: { period }
  })
}

// 热门座位区域
export function getAreaStats() {
  return request({
    url: '/ticket/stats/area',
    method: 'get'
  })
}

// 概览统计
export function getStatsSummary() {
  return request({
    url: '/ticket/stats/summary',
    method: 'get'
  })
}
