import React, { useMemo, useState } from 'react';
import { Link, useNavigate } from 'react-router-dom'
import { useWordsFunctions } from '../components/wordsHandle.js';
import { useGroups } from '../components/groupHangler.js';
import { useWords } from '../components/groupHangler';
import '../styles/HomePage.css';
import { deleteAccount } from './api.jsx';

const HomePage = () => {
  const {
    words, setWords,
    foreignWord, setForeignWord,
    translation, setTranslation,
    selectedWords, setSelectedWords,
    allSelected, setAllSelected,
    editingWord, setEditingWord,
    editValues, setEditValues,
    navigate = useNavigate()
  } = useWords();

  const {
    handleAddWord,
    toggleSelectWord,
    toggleSelectAllWords,
    handleDeleteSelected,
    handleUpdateSelected,
    handleUpdateSubmit
  } = useWordsFunctions({
    words,
    setWords,
    selectedWords,
    setSelectedWords,
    setAllSelected,
    editingWord,
    setEditingWord,
    editValues,
    setEditValues,
    setForeignWord,
    setTranslation,
  });

  const {
    groupName, setGroupName,
    selectedGroupId, setSelectedGroupId,
    groups,
    editingGroupId, setEditingGroupId,
    editingGroupName, setEditingGroupName,
    groupWordsMap,
    expandedGroups,
    selectedGroupWords, setSelectedGroupWords,
    handleAddGroup,
    handleSelectGroup,
    handleRenameGroup,
    handleRemoveWordFromGroup,
    toggleGroupWord,
    toggleSelectAllGroupWords,
    handleDeleteGroup,
    handleAddWordToGroup,
    fetchGroupWords,
  } = useGroups();

  const sortedWords = useMemo(
    () => [...words].sort((a, b) => (Number(b?.id) || 0) - (Number(a?.id) || 0)),
    [words]
  );

  const sortedGroups = useMemo(
    () => [...groups].sort((a, b) => (Number(b?.id) || 0) - (Number(a?.id) || 0)),
    [groups]
  );

  const [isAccountMenuOpen, setIsAccountMenuOpen] = useState(false);
  const [showDeleteModal, setShowDeleteModal] = useState(false);
  const [deleteCode, setDeleteCode] = useState('');
  const [deleteInput, setDeleteInput] = useState('');
  const [isDeleting, setIsDeleting] = useState(false);
  const [deleteError, setDeleteError] = useState(null);

  const openDeleteModal = () => {
    const randomCode = Math.random().toString(36).slice(2, 8).toUpperCase();
    setDeleteCode(randomCode);
    setDeleteInput('');
    setDeleteError(null);
    setShowDeleteModal(true);
    setIsAccountMenuOpen(false);
  };

  const handleDeleteAccount = async () => {
    setIsDeleting(true);
    setDeleteError(null);
    try {
      await deleteAccount();
      sessionStorage.removeItem("authenticated");
      navigate('/');
    } catch (err) {
      setDeleteError(err.message || 'Failed to delete account');
    } finally {
      setIsDeleting(false);
    }
  };

  return (
    <div className="page-container">
      <div className="account-bar">
        <div className="account-menu">
          <button
            className="account-button"
            onClick={() => setIsAccountMenuOpen(!isAccountMenuOpen)}
            title="Account"
          >
            ☰
          </button>
          {isAccountMenuOpen && (
            <div className="account-dropdown">
              <button className="account-action danger" onClick={openDeleteModal}>
                Delete account
              </button>
            </div>
          )}
        </div>
      </div>
      <div className="page-layout">
        <div className="word-section">
          <h2 className='section-title'>Add Word</h2>
          <form
            className="add-word-form"
            onSubmit={async e => {
              e.preventDefault();
              const groupIdNum = selectedGroupId ? Number(selectedGroupId) : null;
              const newWord = await handleAddWord(foreignWord, translation, selectedGroupId);
              if (groupIdNum && expandedGroups.includes(groupIdNum)) {
                fetchGroupWords(groupIdNum);
              }
              setSelectedGroupId('');
            }}
          >
            <input
              type="text"
              placeholder="Foreign word"
              value={foreignWord}
              onChange={e => setForeignWord(e.target.value)}
            />
            <input
              type="text"
              placeholder="Translation"
              value={translation}
              onChange={e => setTranslation(e.target.value)}
            />
            <div className="actions-container select">
              <select
                value={selectedGroupId || ''}
                onChange={e => setSelectedGroupId(e.target.value)}
              >
                <option value="">Select group</option>
                {sortedGroups.map((group) => (
                  <option key={group.id} value={group.id}>
                    {group.name}
                  </option>
                ))}
              </select>
            </div>
            <div className="actions-container button">
              <button type="submit" className='group-button'>Add</button>
            </div>
          </form>

          {/* Word List */}
          <h2 className='section-title'>List of Words</h2>
          <div className='word-table-wrapper'>
          <table className="word-table">
            <thead>
              <tr>
                <th className='section-text' style={{ width: '50%' }}>Foreign Word</th>
                <th className='section-text' style={{ width: '40%' }}>Translation</th>
                <th style={{ width: '10%' }}>
                  <input
                    type="checkbox"
                    checked={allSelected}
                    onChange={() => toggleSelectAllWords(sortedWords, selectedWords, setSelectedWords, setAllSelected)}
                    title="Select all"
                  />
                </th>
              </tr>
            </thead>
            <tbody>
              {sortedWords.map((word, index) => (
                <tr key={index}>
                  <td>{word.foreignWord}</td>
                  <td>{word.translatedWord}</td>
                  <td>
                    <input
                      type="checkbox"
                      checked={selectedWords.includes(word.id)}
                      onChange={() => toggleSelectWord(selectedWords, setSelectedWords, word.id)}
                    />
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
          </div>

          {/* Word Actions */}
          {(() => {
            const combinedSelected = [...selectedWords, ...Object.values(selectedGroupWords).flat()];
            return (
              <div className="actions-container">
                <button
                  onClick={async () => {
                    await handleDeleteSelected(
                      combinedSelected,
                      setWords,
                      words,
                      setSelectedWords,
                      setEditingWord,
                      setEditValues,
                      editingWord
                    );
                  
                    setSelectedWords([]);
                    setSelectedGroupWords({});
                  
                    for (const groupId of expandedGroups) {
                      await fetchGroupWords(groupId);
                    }
                  
                    const refreshed = await getAllWords();
                    setWords(refreshed);
                  }}
                  className="group-button"
                >
                  Delete
                </button>
                <button
                  onClick={() =>
                    handleUpdateSelected(
                      combinedSelected,
                      words,
                      setEditingWord,
                      setEditValues
                    )
                  }
                  className="group-button"
                >
                  Rename
                </button>
                <select
                  value={selectedGroupId}
                  onChange={(e) => setSelectedGroupId(e.target.value)}
                >
                  <option value="" className="group-button">
                    Select group
                  </option>
                  {sortedGroups.map((group) => (
                    <option key={group.id} value={group.id}>
                      {group.name}
                    </option>
                  ))}
                </select>
                <button
                  className="group-button"
                  onClick={async () => {
                    for (const wordId of combinedSelected) {
                      await handleAddWordToGroup(wordId);
                    }
                  }}
                  disabled={combinedSelected.length === 0 || !selectedGroupId}
                >
                  Assign to Group
                </button>
              </div>
            );
          })()}

          {editingWord && (
            <div className="edit-word-form">
              <h3>Edit Word</h3>
                <form onSubmit={handleUpdateSubmit}>
                  <input
                    type="text"
                    value={editValues.foreignWord}
                    onChange={(e) =>
                      setEditValues({ ...editValues, foreignWord: e.target.value })
                    }
                    placeholder="Foreign word"
                  />

                  <input
                    type="text"
                    value={editValues.translatedWord}
                    onChange={(e) =>
                      setEditValues({ ...editValues, translatedWord: e.target.value })
                    }
                    placeholder="Translation"
                  />

                <button type="submit">
                  Confirm Update
                </button>
              </form>
            </div>
          )}

          {/* Practice Link */}
          <div>
            {(() => {
              const practiceIds = [
                ...selectedWords,
                ...Object.values(selectedGroupWords).flat()
              ];
              return (
                <Link
                  to="/practice"
                  state={{ selectedIds: practiceIds }}
                  className="button-link"
                >
                  Start Practice
                </Link>
              );
            })()}
          </div>
        </div>

      <div className="group-section">
        <div>
          <h2 className='section-title'>Groups</h2>
            <form className="add-word-form" onSubmit={handleAddGroup}>
              <input
                type="text"
                placeholder="Group name"
                value={groupName}
                onChange={(e) => setGroupName(e.target.value)}
              />
              <button type="submit" className='group-button'>Add Group</button>
            </form>
        </div>
          <div>
            {sortedGroups.map((group) => (
              <div key={group.id} className="group-conteiner">
                <div className="group-header">
                  <button
                    className="group-button"
                    onClick={() => handleSelectGroup(group.id)}
                  >
                    {editingGroupId === group.id ? (
                      <form onSubmit={(e) => {
                        e.preventDefault();
                        handleRenameGroup(group.id);
                      }}>
                        <input
                          type="text"
                          value={editingGroupName}
                          onChange={(e) => setEditingGroupName(e.target.value)}
                          autoFocus
                        />
                      </form>
                    ) : (
                      group.name
                    )}
                  </button>
                  <button
                    className="delete-group-button"
                    onClick={() => handleDeleteGroup(group.id)}
                    title="Delete group"
                  >
                    ×
                  </button>
                  <button
                    className="rename-group-button"
                    onClick={() => {
                      if (editingGroupId === group.id) {
                        handleRenameGroup(group.id);
                      } else {
                        setEditingGroupId(group.id);
                        setEditingGroupName(group.name);
                      }
                    }}
                    title="Rename group"
                  >
                    ✎
                  </button>
                </div>
                {expandedGroups.includes(group.id) && groupWordsMap[group.id] && (
                  <div>
                    <label>
                      <input
                        type="checkbox"
                        checked={
                          selectedGroupWords[group.id]?.length === groupWordsMap[group.id].length
                        }
                        onChange={() => toggleSelectAllGroupWords(group.id)}
                      />
                      Select All
                    </label>
                    <ul className="group-word-list">
                      {groupWordsMap[group.id].map(word => (
                        <li key={word.id}>
                          <label>
                            <input
                              type="checkbox"
                              checked={selectedGroupWords[group.id]?.includes(word.id) || false}
                              onChange={() => toggleGroupWord(group.id, word.id)}
                            />
                            {word.foreignWord} - {word.translatedWord}
                          </label>
                          <button
                            className="remove-from-group-button"
                            onClick={() => handleRemoveWordFromGroup(word.id, group.id)}
                            title="Remove word from group"
                          >
                            ×
                          </button>
                        </li>
                      ))}
                    </ul>
                  </div>
                )}
              </div>
            ))}
          </div>
        </div>
      </div>
      {showDeleteModal && (
        <div className="modal-backdrop">
          <div className="modal">
            <h3 className="section-title">Delete account</h3>
            <p className="section-text">
              To confirm deletion, type this code: <strong>{deleteCode}</strong>
            </p>
            <input
              type="text"
              value={deleteInput}
              onChange={(e) => setDeleteInput(e.target.value.toUpperCase())}
              placeholder="Enter code"
              className="modal-input"
            />
            {deleteError && <div className="modal-error">{deleteError}</div>}
            <div className="modal-actions">
              <button
                className="group-button"
                onClick={() => setShowDeleteModal(false)}
                disabled={isDeleting}
              >
                Cancel
              </button>
              <button
                className="group-button danger"
                onClick={handleDeleteAccount}
                disabled={deleteInput.trim() !== deleteCode || isDeleting}
              >
                {isDeleting ? 'Deleting...' : 'Delete account'}
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};

export default HomePage;
