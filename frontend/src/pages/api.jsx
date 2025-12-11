export const getPracticeWords = async (ids) => {
    const response = await fetch(`/api/practice?ids=${ids.join(',')}`);
    if (!response.ok) throw new Error('Error loading words');
    return await response.json();
  };
  
export const checkPracticeAnswer = async ({ id, userWord, direction }) => {
    console.log('Sending practice answer:', { id, userWord, direction });
    const response = await fetch('/api/practice', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({
        id,
        userWord,
        direction
      })
    });
    if (!response.ok) throw new Error('Error while checking words');
    return await response.json();
  };

export const saveWord = async ({ foreignWord, translatedWord, groupId }) => {
    const response = await fetch(`/api/words`, {
      method: 'POST',
      credentials: "include",
      headers: {
        "Content-Type": "application/json"
      },
      body: JSON.stringify({
        foreignWord,
        translatedWord,
        groupId: groupId ?? null
      })
    });
  
    if (!response.ok) throw new Error('Failed to save word');
    return await response.json();
  };

export const getAllWords = async () => {
  const response = await fetch('/api', {
    method: 'GET'
  });
  if (!response.ok) throw new Error('Failed to load words');
  return await response.json();
};

export const deleteWords = async (ids) => {
  for (const id of ids) {
    const response = await fetch(`/api/${id}`, {
      method: 'DELETE'
    });
    if (!response.ok) throw new Error('Failed to delete word');
  }
};

export const updateWord = async ({ id, foreignWord, translatedWord }) => {
  const response = await fetch('/api', {
    method: 'PUT',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ id, foreignWord, translatedWord })
  });
  if (!response.ok) throw new Error('Failed to update word');
};

export const createWordGroup = async (groupName) => {
  const response = await fetch(`/api/word-groups/${groupName}`, {
    method: 'POST'
  });
  if (!response.ok) throw new Error('Failed to create group');
  return await response.json();
};

export const getAllGroups = async () => {
    const response = await fetch('/api/word-groups/groups', {
      method: 'GET'
    });
    if (!response.ok) throw new Error('Failed to load groups');
    return await response.json();
  };

export const getWordsByGroup = async (groupId) => {
    const response = await fetch(`/api/word-groups/words?groupId=${groupId}`, {
      method: 'GET'
    });
    if (!response.ok) throw new Error('Failed to load group words');
    return await response.json();
  };

export const deleteGroup = async (groupId) => {
    const response = await fetch(`/api/word-groups/deleteGroup/${groupId}`, {
      method: 'DELETE'
    });
    if (!response.ok) throw new Error('Failed to delete group');
};

export const renameGroup = async (groupId, newGroupName) => {
    const response = await fetch(`/api/word-groups/rename`, {
      method: 'PUT',
      credentials: 'include',
      headers: {
        "Content-Type": "application/json"
      },
      body: JSON.stringify({ 
        id: groupId, 
        name: newGroupName 
      })
    });

    if (!response.ok) throw new Error('Failed to rename group');
};

export const removeWordFromGroup = async (wordId) => {
    const response = await fetch(`/api/word-groups/delete/${wordId}`, {
      method: 'DELETE'
    });
    if (!response.ok) throw new Error('Failed to remove word from group');
};

export const addWordToGroup = async (groupId, wordId) => {
    const response = await fetch(`/api/word-groups/addToGroup/${groupId}/${wordId}`, {
      method: 'PUT'
    });
    if (!response.ok) throw new Error('Failed to add word to group');
  };

export const requestPasswordReset = async (email) => {
  const response = await fetch('/auth/forgot-password', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ email })
  });

  const text = await response.text();
  if (!response.ok) throw new Error(text || 'Failed to send reset email');
  return text;
};

export const resetPassword = async ({ token, newPassword }) => {
  const response = await fetch('/auth/reset-password', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ token, newPassword })
  });

  const text = await response.text();
  if (!response.ok) throw new Error(text || 'Failed to reset password');
  return text;
};

export const validateResetToken = async (token) => {
  const response = await fetch(`/auth/reset-password/validate?token=${encodeURIComponent(token)}`, {
    method: 'GET'
  });
  const text = await response.text();
  if (!response.ok) throw new Error(text || 'Invalid token');
  return text;
};

export const deleteAccount = async () => {
  const response = await fetch('/account/delete', {
    method: 'POST',
    credentials: 'include',
  });
  const text = await response.text();
  if (!response.ok) throw new Error(text || 'Failed to delete account');
  return text;
};

export const logout = async () => {
  const response = await fetch('/auth/logout', {
    method: 'POST',
    credentials: 'include',
  });
  if (!response.ok) {
    const text = await response.text();
    throw new Error(text || 'Failed to logout');
  }
};
