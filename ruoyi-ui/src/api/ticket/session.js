import request from '@/utils/request'

// 查询场次列表
export function listSession(query) {
  return request({
    url: '/ticket/session/list',
    method: 'get',
    params: query
  })
}

// 查询场次详细
export function getSession(sessionId) {
  return request({
    url: '/ticket/session/' + sessionId,
    method: 'get'
  })
}

// 新增场次
export function addSession(data) {
  return request({
    url: '/ticket/session',
    method: 'post',
    data: data
  })
}

// 修改场次
export function updateSession(data) {
  return request({
    url: '/ticket/session/edit',
    method: 'post',
    data: data
  })
}

// 删除场次
export function delSession(sessionIds) {
  return request({
    url: '/ticket/session/remove',
    method: 'post',
    params: { sessionIds }
  })
}

// 批量生成场次座位
export function batchGenerateSeats(data) {
  return request({
    url: '/ticket/session/batchGenerateSeats',
    method: 'post',
    params: data
  })
}
